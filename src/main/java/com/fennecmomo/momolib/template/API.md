# MomoLib 公开 API 参考

> 本文档仅列出允许外部模块调用的 API，内部实现类不在此列。

---

## 一、AI 行为框架

### WeightedChildBehavior — 加权子行为基类

继承此类编写可被 WeightedPicker 调度的女仆 AI 行为。

```java
import com.fennecmomo.momolib.behavior.WeightedChildBehavior;
```

#### 构造（无时长限制）

```java
protected WeightedChildBehavior(Map<MemoryModuleType<?>, MemoryStatus> requiredMemoryState)
```

| 参数 | 含义 |
|---|---|
| requiredMemoryState | 启动此行为所需的记忆状态映射 |

#### 构造（有最大时长）

```java
protected WeightedChildBehavior(Map<MemoryModuleType<?>, MemoryStatus> requiredMemoryState, int maxDuration)
```

| 参数 | 含义 |
|---|---|
| requiredMemoryState | 启动此行为所需的记忆状态映射 |
| maxDuration | 最长持续 tick 数，超时自动停止 |

#### 上报权重（子类必须实现）

```java
public abstract int getWeight(EntityMaid maid)
```

| 参数 | 含义 |
|---|---|
| maid | 当前女仆实体 |
| 返回 | 非负整数权重，0 = 不参与选择 |

#### 生命周期方法（子类重写）

```java
public boolean canStart(ServerLevel level, EntityMaid maid)
public boolean canContinue(ServerLevel level, EntityMaid maid, long time)
public void begin(ServerLevel level, EntityMaid maid, long time)
public void update(ServerLevel level, EntityMaid maid, long time)
public void end(ServerLevel level, EntityMaid maid, long time)
```

---

### WeightedPicker — 加权随机行为容器

放入一组 WeightedChildBehavior，每次启动时按权重轮盘赌随机选一个独占执行。

```java
import com.fennecmomo.momolib.behavior.WeightedPicker;
```

#### 构造

```java
public WeightedPicker(List<WeightedChildBehavior> entries)
```

| 参数 | 含义 |
|---|---|
| entries | 初始子行为列表 |

#### 注册子行为

```java
public void register(WeightedChildBehavior b)
```

| 参数 | 含义 |
|---|---|
| b | 要追加的子行为 |

---

## 二、UI 模板

### ConfirmPopup — 确认弹窗

服务端发起、客户端渲染的双按钮模态对话框，用于让玩家确认/拒绝某个操作。

```java
import com.fennecmomo.momolib.template.Data.ConfirmPopupMenu;
```

#### 调起双按钮确认弹窗

向指定玩家弹出一个确认窗口，玩家点击左/右按钮后自动发送对应命令并关闭。

```java
public static void open(ServerPlayer player, String title, String info, String leftLabel, String leftCommand, String rightLabel, String rightCommand)
```

| 参数 | 含义 |
|---|---|
| player | 服务端玩家 |
| title | 弹窗标题 |
| info | 详细信息（空字符串不显示） |
| leftLabel | 左按钮文字 |
| leftCommand | 左按钮点击后发送的命令 |
| rightLabel | 右按钮文字 |
| rightCommand | 右按钮点击后发送的命令 |

---

### GenericContainerMenu — 通用容器菜单基类

继承 AbstractContainerMenu，封装动态行列布局、槽位自动添加和 Shift 点击转移。子类只需传入行列数和容器即可使用。

```java
import com.fennecmomo.momolib.template.Data.GenericContainerMenu;
```

#### 构造

```java
protected GenericContainerMenu(MenuType<?> type, int containerId, int cols, int rows, Container container, Inventory inv)
```

| 参数 | 含义 |
|---|---|
| type | 菜单类型（由注册表提供） |
| containerId | 容器会话 ID |
| cols | 容器列数 |
| rows | 容器行数 |
| container | 底层物品容器 |
| inv | 玩家背包 |

#### 尺寸访问器

```java
public int getImageWidth()
public int getImageHeight()
```

#### 布局计算（静态方法）

```java
public static int containerY()
public static int playerY(int rows)
public static int hotbarY(int rows)
```

#### 可重写扩展点

```java
protected Slot createContainerSlot(Container container, int index, int x, int y)
protected void addContainerSlots(Container container)
public ItemStack quickMoveStack(Player player, int index)
public boolean stillValid(Player player)
```

| 方法 | 用途 |
|---|---|
| createContainerSlot | 返回自定义 Slot（如超大堆叠） |
| addContainerSlots | 自定义槽位添加逻辑 |
| quickMoveStack | 自定义 Shift 点击转移规则 |
| stillValid | 自定义有效性判断（如绑定方块实体） |

#### 布局常量

| 常量 | 含义 |
|---|---|
| PAD | 背景边距（4px） |
| TITLE_H | 标题栏高度（18px） |
| GAP | 容器与背包间距（18px） |
| SLOT | 格子像素大小（18px） |
| PLAYER_COLS | 玩家背包列数（9） |
| PLAYER_ROWS | 玩家背包行数（3） |
| HOTBAR_ROWS | 热键栏行数（1） |

---

### GenericContainerScreen — 通用容器屏幕基类

继承 AbstractContainerScreen，配合 GenericContainerMenu 使用。内置蒙版、灰色面板、边框和标题渲染。

```java
import com.fennecmomo.momolib.template.UI.GenericContainerScreen;
```

#### 构造

```java
public GenericContainerScreen(T menu, Inventory inv, Component title)
```

| 参数 | 含义 |
|---|---|
| menu | GenericContainerMenu 子类实例 |
| inv | 玩家背包 |
| title | 屏幕标题 |

子类直接继承即可使用，无需额外代码。如需自定义渲染（如超大堆叠数量显示），重写 `extractRenderState` 并在 `super` 调用前后添加自定义逻辑。

---

### IListItem — 滚动列表条目接口

所有放入 ContentList 的组件必须实现此接口。

```java
import com.fennecmomo.momolib.template.Interface.IListItem;
```

```java
void setEntryWidth(int w)
void setEntryPosition(int x, int y)
void activate()
void deactivate()
```

| 参数 | 含义 |
|---|---|
| w | 条目宽度 |
| x, y | 条目左上角坐标 |

---

### IBar — 进度条渲染器

定义进度条的绘制方式，StateBar 组合使用。

```java
import com.fennecmomo.momolib.template.Interface.IBar;
```

```java
void draw(GuiGraphicsExtractor g, int x, int y, int w, int h, float value)
```

| 参数 | 含义 |
|---|---|
| g | 图形提取器 |
| x, y, w, h | 进度条区域 |
| value | 0.0~1.0 的当前值 |

#### 内置实现

| 常量 | 说明 |
|---|---|
| `IBar.DEFAULT` | 灰底 + 绿(>50%)/橙(>20%)/红 前景 |

实现此接口可自定义进度条皮肤，传给 `StateBar.withBarRenderer()`。

---

### ContentList — 可滚动条目列表

管理一组 IListItem，配合内置 ScrollBar 实现虚拟滚动（只渲染可见行）。

```java
import com.fennecmomo.momolib.template.List.ContentList;
```

#### 构造

```java
public ContentList(Consumer<AbstractWidget> adder, int x, int y, int w, int h, int rowH)
```

| 参数 | 含义 |
|---|---|
| adder | widget 注册回调（传 `screen::addRenderableWidget`） |
| x, y | 列表区域位置 |
| w | 列表宽度 |
| h | 列表高度 |
| rowH | 每行像素高度 |

#### 条目管理

```java
public void setEntries(List<IListItem> items)
public void refresh()
```

#### 事件转发

```java
public boolean mouseDragged(MouseButtonEvent e, double dx, double dy)
public boolean mouseScrolled(double mx, double my, double sx, double sy)
```

---

### ButtonListItem — 按钮条目

支持三种模式：纯按钮、文本+按钮、纯文本。实现 IListItem 可放入 ContentList。

```java
import com.fennecmomo.momolib.template.List.ButtonListItem;
```

#### 构造（纯按钮）

```java
public ButtonListItem(int x, int y, int w, int h, Component label, Button.OnPress onPress, Consumer<AbstractWidget> adder)
```

| 参数 | 含义 |
|---|---|
| x, y | 按钮位置 |
| w, h | 按钮尺寸 |
| label | 按钮文字 |
| onPress | 点击回调 |
| adder | widget 注册回调 |

#### 构造（文本+按钮）

```java
public ButtonListItem(int x, int y, int w, int h, Font font, Component text, String btnLabel, int btnW, int btnH, Button.OnPress onPress, Consumer<AbstractWidget> adder)
```

| 参数 | 含义 |
|---|---|
| x, y | 条目位置 |
| w, h | 条目尺寸 |
| font | 字体 |
| text | 左侧文字 |
| btnLabel | 按钮文字 |
| btnW, btnH | 按钮尺寸 |
| onPress | 按钮点击回调 |
| adder | widget 注册回调 |

#### 构造（纯文本）

```java
public ButtonListItem(int x, int y, int w, int h, Font font, Component text, Consumer<AbstractWidget> adder)
```

#### 配置

```java
public void setText(Component t)
public void setTextColor(int c)
```

---

### InputListItem — 输入框条目

包装原版 EditBox 为 IListItem，可放入 ContentList。

```java
import com.fennecmomo.momolib.template.List.InputListItem;
```

#### 构造

```java
public InputListItem(int x, int y, int w, int h, Font font, Component hint, Consumer<AbstractWidget> adder)
```

| 参数 | 含义 |
|---|---|
| x, y | 位置 |
| w, h | 尺寸 |
| font | 字体 |
| hint | 占位提示文字 |
| adder | widget 注册回调 |

#### 配置

```java
public EditBox getInput()
public void setFilter(Predicate<String> f)
public void setMaxLength(int n)
```

---

### StateBar — 状态条

显示"标签 + 数值 + 进度条"三栏，用于属性展示。实现 IListItem 可放入 ContentList。

```java
import com.fennecmomo.momolib.template.Bar.StateBar;
```

#### 构造

```java
public StateBar(int x, int y, int w, int h, Font font, String label, float value, Consumer<AbstractWidget> adder)
```

| 参数 | 含义 |
|---|---|
| x, y | 位置 |
| w, h | 尺寸 |
| font | 字体 |
| label | 状态名称 |
| value | 当前值 |
| adder | widget 注册回调 |

#### 链式配置

```java
public StateBar withLabelColor(int c)
public StateBar withValueColor(int c)
public StateBar withBarRenderer(IBar r)
public StateBar withMax(float m)
public StateBar withFmt(Fmt f)
public StateBar withValText(String t)
public void setValue(float v)
```

| 参数 | 含义 |
|---|---|
| c | 颜色值 |
| r | 自定义进度条渲染器 |
| m | 最大值（默认 100） |
| f | 数值格式：RATIO / INTEGER / EXP |
| t | 覆盖数值文本（如 "Lv.5"） |
