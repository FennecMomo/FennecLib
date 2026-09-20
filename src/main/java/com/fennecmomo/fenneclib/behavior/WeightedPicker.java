package com.fennecmomo.fenneclib.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;

// 加权随机行为容器。
//
// 自身继承 MC 原版 Behavior<EntityMaid>，内部维护一组 WeightedChildBehavior 子行为。
// 每次启动时按各子行为上报的权重做随机选择，选中一个独占执行，
// 直到该子行为结束后，下次 tick 才会重新加权随机选下一个。
//
// 生命周期流转（Brain 每 tick 调度）：
//   1. checkExtraStartConditions  ->  遍历子行为，任一 canStart=true 则返回 true
//   2. start                      ->  pick() 加权随机选一个子行为，调用 begin()
//   3. tick (每 tick)              ->  转发给当前子行为的 update()
//   4. canStillUse                 ->  委托给当前子行为的 canContinue()
//   5. stop                        ->  调用当前子行为的 end()，清空 current
//
public class WeightedPicker extends Behavior<EntityMaid>
{
    // ===================== 字段 =====================

    // 所有已注册的子行为列表
    private final List<WeightedChildBehavior> entries;
    // 当前正在执行的子行为，null 表示无行为在执行
    private WeightedChildBehavior current;

    // ===================== 构造器 =====================
    // 创建一个加权随机行为容器。无记忆要求，持续时间为 MAX_VALUE，由子行为自行控制结束。
    // entries: 初始子行为列表（可变列表，后续可通过 register() 追加）
    public WeightedPicker(List<WeightedChildBehavior> entries)
    {
        super(Map.of(), Integer.MAX_VALUE);
        this.entries = entries;
    }

    // ===================== 注册 =====================
    // 动态追加一个子行为到列表末尾。
    // b: 要注册的子行为
    public void register(WeightedChildBehavior b)
    {
        entries.add(b);
    }

    // ===================== 加权随机选择 =====================
    // 从所有满足启动条件的子行为中按权重随机选一个（经典轮盘赌算法）：
    //   1. 遍历所有子行为，过滤出 canStart=true 的候选项，累加总权重
    //   2. 生成 [0, totalWeight) 的随机数 roll
    //   3. 逐个减去各候选项权重，第一个使 roll<0 的即为选中
    // level: 服务端世界
    // maid:  当前女仆实体
    // 返回: 选中的子行为，若无候选项则返回 null
    public WeightedChildBehavior pick(ServerLevel level, EntityMaid maid)
    {
        List<WeightedChildBehavior> candidates = new ArrayList<>();
        int totalWeight = 0;
        for (WeightedChildBehavior b : entries)
        {
            if (b.canStart(level, maid))
            {
                candidates.add(b);
                totalWeight += b.getWeight(maid);
            }
        }
        if (candidates.isEmpty())
        {
            return null;
        }

        int roll = maid.getRandom().nextInt(totalWeight);
        for (WeightedChildBehavior b : candidates)
        {
            roll -= b.getWeight(maid);
            if (roll < 0)
            {
                return b;
            }
        }
        return candidates.get(0);
    }

    // ===================== 条件判断 =====================
    // 只要有一个子行为可启动，容器就可以启动。
    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, EntityMaid maid)
    {
        if (entries.isEmpty())
        {
            return false;
        }
        for (WeightedChildBehavior b : entries)
        {
            if (b.canStart(level, maid))
            {
                return true;
            }
        }
        return false;
    }

    // 容器能否继续使用取决于当前子行为能否继续。若无当前行为则返回 false，触发 Brain 重新调度。
    @Override
    protected boolean canStillUse(ServerLevel level, EntityMaid maid, long time)
    {
        if (current == null)
        {
            return false;
        }
        return current.canContinue(level, maid, time);
    }

    // ===================== 生命周期 =====================
    // 启动时加权随机选一个子行为并开始执行。
    @Override
    protected void start(ServerLevel level, EntityMaid maid, long time)
    {
        current = pick(level, maid);
        if (current != null)
        {
            current.begin(level, maid, time);
        }
    }

    // 每 tick 转发给当前子行为，若无当前行为则跳过。
    @Override
    protected void tick(ServerLevel level, EntityMaid maid, long time)
    {
        if (current == null)
        {
            return;
        }
        current.update(level, maid, time);
    }

    // 停止时结束当前子行为并清空引用，等待下次重新选择。
    @Override
    protected void stop(ServerLevel level, EntityMaid maid, long time)
    {
        if (current != null)
        {
            current.end(level, maid, time);
            current = null;
        }
    }
}
