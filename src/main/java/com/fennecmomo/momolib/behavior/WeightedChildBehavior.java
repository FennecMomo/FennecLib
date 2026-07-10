package com.fennecmomo.momolib.behavior;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;

import java.util.Map;

// 加权随机子行为的基类。把 Behavior 的 protected 生命周期方法桥接为 public，
// 让 ColonistWeightedBehavior 容器可以统一调度。
public abstract class WeightedChildBehavior extends Behavior<EntityMaid>
{

    protected WeightedChildBehavior(Map<net.minecraft.world.entity.ai.memory.MemoryModuleType<?>, net.minecraft.world.entity.ai.memory.MemoryStatus> requiredMemoryState)
    {
        super(requiredMemoryState);
    }

    protected WeightedChildBehavior(Map<net.minecraft.world.entity.ai.memory.MemoryModuleType<?>, net.minecraft.world.entity.ai.memory.MemoryStatus> requiredMemoryState, int maxDuration)
    {
        super(requiredMemoryState, maxDuration);
    }

    // ======== 权重 ========

    // 返回当前权重值，Picker 据此加权随机选择
    public abstract int getWeight(EntityMaid maid);

    // ======== 桥接方法（protected → public） ========

    // 包装 checkExtraStartConditions
    public boolean canStart(ServerLevel level, EntityMaid maid)
    {
        return checkExtraStartConditions(level, maid);
    }

    // 包装 canStillUse
    public boolean canContinue(ServerLevel level, EntityMaid maid, long time)
    {
        return canStillUse(level, maid, time);
    }

    // 包装 start
    public void begin(ServerLevel level, EntityMaid maid, long time)
    {
        start(level, maid, time);
    }

    // 包装 tick
    public void update(ServerLevel level, EntityMaid maid, long time)
    {
        tick(level, maid, time);
    }

    // 包装 stop
    public void end(ServerLevel level, EntityMaid maid, long time)
    {
        stop(level, maid, time);
    }
}
