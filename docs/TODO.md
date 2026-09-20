# FennecLib TODO

## behavior — 加权算法框架解耦

### 现状

`WeightedChildBehavior` 和 `WeightedPicker` 直接继承 TLM 的 `Behavior<EntityMaid>`，加权随机选择逻辑与行为类生命周期强绑定。

### 目标

将加权算法从行为类中剥离，形成独立可用的通用框架：

1. **提取核心算法**：加权轮盘赌选择、权重上报接口等核心逻辑抽为独立类/接口，不依赖任何行为类
2. **任意位置可调用**：提取后的框架可在任何地方直接使用（不限于 Brain/Behavior 体系）
3. **可选外壳包装**：提供一层 `Behavior` 适配器壳，使框架可以挂到 Brain 行为树上使用，但不强制绑定
4. **不绑死行为类**：核心算法类不继承 `Behavior`，不依赖 `EntityMaid`，不耦合 TLM

### 涉及文件

- `fenneclib/behavior/WeightedChildBehavior.java` — 当前继承 `Behavior<EntityMaid>`
- `fenneclib/behavior/WeightedPicker.java` — 当前继承 `Behavior<EntityMaid>`
