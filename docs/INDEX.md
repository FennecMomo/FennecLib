# FennecLib 文档

> 版本：0.0.1 | MC 26.1.2 / NeoForge | JDK 25

MaidTown 模组系列的基础库，提供 AI 行为框架和 UI 模板组件。

## 文档列表

| 文档 | 说明 |
|------|------|
| [API.md](API.md) | 公开 API 参考：WeightedPicker/WeightedChildBehavior、ConfirmPopup、GenericContainerMenu/Screen、ContentList、StateBar 等 |
| [TODO.md](TODO.md) | 待办：加权算法框架与 TLM Behavior 解耦 |

## 包结构

```
com.fennecmomo.fenneclib/
  FennecLib.java                          # @Mod 入口
  behavior/
    WeightedPicker.java                 # 加权随机行为容器
    WeightedChildBehavior.java          # 加权子行为基类
  template/
    API.md                              # 公开 API 文档
    Bar/
      ScrollBar.java                    # 滚动条组件
      StateBar.java                     # 状态条（标签+数值+进度）
    Data/
      ConfirmPopupMenu.java             # 确认弹窗菜单
      GenericContainerMenu.java         # 通用容器菜单
    Interface/
      IBar.java                         # 进度条渲染接口
      IListItem.java                    # 列表条目接口
    List/
      ButtonListItem.java               # 按钮条目
      ContentList.java                  # 可滚动条目列表
      InputListItem.java                # 输入框条目
    Register/
      TemplateRegistration.java         # 服务端注册
      TemplateClientRegistration.java   # 客户端注册
    UI/
      ConfirmPopupScreen.java           # 确认弹窗屏幕
      GenericContainerScreen.java       # 通用容器屏幕
```
