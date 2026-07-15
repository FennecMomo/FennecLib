package com.fennecmomo.momolib.behavior;

import java.util.Map;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

// 加权随机子行为基类。
//
// 继承 MC 原版 Behavior<EntityMaid>，在此基础上做两件事：
// 1. 定义 getWeight(maid) 抽象方法，让子类上报权重供 WeightedPicker 做加权随机选择。
// 2. 将 Behavior 的 protected 生命周期方法桥接为 public，使 WeightedPicker 容器可以从外部统一调度。
//
// 桥接映射表：
//   Behavior (protected)                    ->  WeightedChildBehavior (public)
//   ---------------------------------------------------------------
//   checkExtraStartConditions(level, maid)  ->  canStart(level, maid)
//   canStillUse(level, maid, time)          ->  canContinue(level, maid, time)
//   start(level, maid, time)                ->  begin(level, maid, time)
//   tick(level, maid, time)                 ->  update(level, maid, time)
//   stop(level, maid, time)                 ->  end(level, maid, time)
//
public abstract class WeightedChildBehavior extends Behavior<EntityMaid>
{
    // ===================== 构造器 =====================

    // 创建一个无最大时长限制的子行为。
    // requiredMemoryState: 启动此行为所需的记忆状态映射（记忆模块 -> 要求状态）
    protected WeightedChildBehavior(Map<MemoryModuleType<?>, MemoryStatus> requiredMemoryState)
    {
        super(requiredMemoryState);
    }

    // 创建一个有最大时长限制的子行为，超时后自动停止。
    // requiredMemoryState: 启动此行为所需的记忆状态映射
    // maxDuration:         行为最长持续 tick 数，超时后自动停止
    protected WeightedChildBehavior(Map<MemoryModuleType<?>, MemoryStatus> requiredMemoryState, int maxDuration)
    {
        super(requiredMemoryState, maxDuration);
    }

    // ===================== 权重 =====================
    // 返回当前权重值，WeightedPicker 据此做加权随机选择。返回值越大被选中概率越高，返回 0 则不会被选中。
    // maid: 当前女仆实体
    // 返回: 非负整数权重
    public abstract int getWeight(EntityMaid maid);

    // ===================== 桥接方法（protected -> public） =====================
    // 桥接 checkExtraStartConditions：判断行为是否可以开始。
    // level: 服务端世界
    // maid:  当前女仆实体
    public boolean canStart(ServerLevel level, EntityMaid maid)
    {
        return checkExtraStartConditions(level, maid);
    }

    // 桥接 canStillUse：判断正在执行的行为是否可以继续。
    // level: 服务端世界
    // maid:  当前女仆实体
    // time:  当前游戏时间
    public boolean canContinue(ServerLevel level, EntityMaid maid, long time)
    {
        return canStillUse(level, maid, time);
    }

    // 桥接 start：行为开始时的初始化逻辑。
    // level: 服务端世界
    // maid:  当前女仆实体
    // time:  当前游戏时间
    public void begin(ServerLevel level, EntityMaid maid, long time)
    {
        start(level, maid, time);
    }

    // 桥接 tick：行为执行中每 tick 调用的逻辑。
    // level: 服务端世界
    // maid:  当前女仆实体
    // time:  当前游戏时间
    public void update(ServerLevel level, EntityMaid maid, long time)
    {
        tick(level, maid, time);
    }

    // 桥接 stop：行为结束时的清理逻辑。
    // level: 服务端世界
    // maid:  当前女仆实体
    // time:  当前游戏时间
    public void end(ServerLevel level, EntityMaid maid, long time)
    {
        stop(level, maid, time);
    }
}
