package com.fennecmomo.momolib.behavior;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 加权随机行为容器。维护一个权重Behavior列表，按各自上报的权重随机选中一个独占执行，
// 直到子行为结束，然后重新加权随机选下一个。
public class WeightedPicker extends Behavior<EntityMaid>
{

    private final List<WeightedChildBehavior> entries;
    private WeightedChildBehavior current;

    public WeightedPicker(List<WeightedChildBehavior> entries)
    {
        super(Map.of(), Integer.MAX_VALUE);
        this.entries = entries;
    }

    // ======== 注册 ========

    public void register(WeightedChildBehavior b)
    {
        entries.add(b);
    }

    // ======== 加权随机选择 ========

    // 从所有 canStart 为 true 的里面按权重随机选一个。
    // 返回 null 表示没有任何一个可启动。
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
        if (candidates.isEmpty()) return null;

        int roll = maid.getRandom().nextInt(totalWeight);
        for (WeightedChildBehavior b : candidates)
        {
            roll -= b.getWeight(maid);
            if (roll < 0) return b;
        }
        return candidates.get(0);
    }

    // ======== 条件 ========

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, EntityMaid maid)
    {
        if (entries.isEmpty()) return false;
        for (WeightedChildBehavior b : entries)
        {
            if (b.canStart(level, maid)) return true;
        }
        return false;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, EntityMaid maid, long time)
    {
        if (current == null) return false;
        return current.canContinue(level, maid, time);
    }

    // ======== 生命周期 ========

    @Override
    protected void start(ServerLevel level, EntityMaid maid, long time)
    {
        current = pick(level, maid);
        if (current != null)
        {
            current.begin(level, maid, time);
        }
    }

    @Override
    protected void tick(ServerLevel level, EntityMaid maid, long time)
    {
        if (current == null) return;
        current.update(level, maid, time);
    }

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
