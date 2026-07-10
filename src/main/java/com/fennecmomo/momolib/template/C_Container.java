package com.fennecmomo.momolib.template;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

// 自定义物品存储容器——可配置行列数的格子容器
// 用于任务板的奖励储存（9×6=54格）和居民背包等场景
// 内置 NBT 序列化支持，变更时自动回调通知
public class C_Container
{
    // 容器背景的边距
    public static final int PAD = 4;
    // 标题栏高度
    public static final int TITLE_H = 18;
    // 容器区域和玩家背包之间的间距
    public static final int GAP = 18;
    // 每个格子的像素大小
    public static final int SLOT = 18;
    // 玩家背包的列数（9格热键栏）
    public static final int PLAYER_COLS = 9;
    // 玩家背包的行数（主物品栏）
    public static final int PLAYER_ROWS = 3;
    // 热键栏行数
    public static final int HOTBAR_ROWS = 1;

    // 容器的列数、行数、总格子数
    private final int cols, rows, slotCount;
    // 底层原版容器
    private final SimpleContainer container;

    // cols/rows 定义容器大小
    // onChanged：容器内容变更时的回调（用于标记方块实体脏数据）
    public C_Container(int cols, int rows, Runnable onChanged)
    {
        this.cols = cols; this.rows = rows; this.slotCount = cols * rows;
        this.container = new SimpleContainer(slotCount)
        {
            // 重写 setChanged，在内容变更时触发外部回调
            @Override public void setChanged()
            {
                super.setChanged();
                if (onChanged != null) onChanged.run();
            }
        };
    }

    // 获取底层原版容器（给 GUI/Menu 用）
    public SimpleContainer getContainer() { return container; }
    public int getCols() { return cols; }
    public int getRows() { return rows; }
    public int getSlotCount() { return slotCount; }

    // 容器界面的整体宽度（容器宽度 = 玩家背包宽度）
    public int imageWidth() { return PAD + PLAYER_COLS * SLOT + PAD; }
    // 容器界面的整体高度
    public int imageHeight() { return PAD + TITLE_H + rows * SLOT + GAP + PLAYER_ROWS * SLOT + HOTBAR_ROWS * SLOT + PAD; }
    // 容器区域起始 Y 坐标
    public int containerY() { return PAD + TITLE_H; }
    // 玩家主物品栏区域起始 Y 坐标
    public int playerY() { return containerY() + rows * SLOT + GAP; }
    // 热键栏区域起始 Y 坐标
    public int hotbarY() { return playerY() + PLAYER_ROWS * SLOT; }

    // 添加容器格子到 GUI
    // factory：可选的自定义格子工厂（null则用默认 Slot）
    public void addContainerSlots(java.util.function.Consumer<Slot> adder, SlotFactory factory)
    {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
            {
                int idx = c + r * cols;
                Slot s = factory != null ? factory.create(container, idx, PAD + c * SLOT, containerY() + r * SLOT)
                        : new Slot(container, idx, PAD + c * SLOT, containerY() + r * SLOT);
                adder.accept(s);
            }
    }
    // 添加容器格子（默认 Slot）
    public void addContainerSlots(java.util.function.Consumer<Slot> adder) { addContainerSlots(adder, null); }

    // 添加玩家背包格子（主物品栏 + 热键栏）
    // inv：打开界面的玩家背包
    public void addPlayerSlots(java.util.function.Consumer<Slot> adder, Inventory inv)
    {
        // 主物品栏（3行×9列，索引从9开始跳过热键栏）
        for (int r = 0; r < PLAYER_ROWS; r++)
            for (int c = 0; c < PLAYER_COLS; c++)
                adder.accept(new Slot(inv, c + r * PLAYER_COLS + PLAYER_COLS, PAD + c * SLOT, playerY() + r * SLOT));
        // 热键栏（1行×9列，索引0-8）
        for (int c = 0; c < PLAYER_COLS; c++)
            adder.accept(new Slot(inv, c, PAD + c * SLOT, hotbarY()));
    }

    // 将容器内容写入 NBT（存档）
    public void saveToTag(ValueOutput out)
    {
        NonNullList<ItemStack> items = NonNullList.withSize(slotCount, ItemStack.EMPTY);
        for (int i = 0; i < slotCount; i++) items.set(i, container.getItem(i));
        ContainerHelper.saveAllItems(out, items);
    }

    // 从 NBT 恢复容器内容（读档）
    public void loadFromTag(ValueInput in)
    {
        NonNullList<ItemStack> items = NonNullList.withSize(slotCount, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(in, items);
        for (int i = 0; i < slotCount; i++) container.setItem(i, items.get(i));
    }

    // 从容器中取出指定数量的物品（用于发放任务奖励）
    // 返回取出的物品堆叠，可能不足 count 如果容器内物品不够
    public ItemStack takeItems(int count)
    {
        ItemStack result = ItemStack.EMPTY;
        for (int i = 0; i < slotCount && count > 0; i++)
        {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty())
            {
                int take = Math.min(stack.getCount(), count);
                result = result.isEmpty() ? stack.split(take) : result;
                if (!result.isEmpty() && result != stack.split(take)) result.grow(take);
                else { result = stack.split(take); }
                count -= take;
                if (stack.isEmpty()) container.setItem(i, ItemStack.EMPTY);
            }
        }
        return result;
    }

    // 自定义格子工厂——允许子类创建特殊格子（如只读展示格）
    @FunctionalInterface
    public interface SlotFactory { Slot create(Container c, int idx, int x, int y); }
}
