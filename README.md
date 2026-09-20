# FennecLib

MaidTown 模组系列的基础库，为女仆工作系统提供通用框架组件。

## 功能

- **加权行为框架**：WeightedPicker + WeightedChildBehavior，支持按权重随机选择子行为
- **UI 模板组件**：容器屏幕模板（C_Container）、入口列表（W_EntryList）、统计条（W_StatBar）、滚动条（W_Scrollbar）等可复用 UI 控件

## 依赖

- MC 26.1.2 + NeoForge
- Touhou Little Maid (TLM) 2.0.0+

## 构建

```bash
./gradlew build
```

产物在 `build/libs/fenneclib-0.0.1.jar`
