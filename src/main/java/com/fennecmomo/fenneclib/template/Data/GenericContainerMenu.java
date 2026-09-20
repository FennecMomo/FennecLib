package com.fennecmomo.fenneclib.template.Data;

import com.fennecmomo.fenneclib.FennecLibConfig;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

// 通用物品容器菜单基类——可配置行列数的格子容器。
// 内置布局常量、动态槽位添加和 Shift 点击物品转移。
// 子类继承后只需补充自定义逻辑（如自定义 Slot 类型、特殊 stillValid 判断）。
public abstract class GenericContainerMenu extends AbstractContainerMenu
{
    // ===================== 布局常量 =====================

    // 容器背景边距
    public static final int PAD = 4;
    // 标题栏高度
    public static final int TITLE_H = 18;
    // 容器区域与玩家背包之间的间距
    public static final int GAP = 18;
    // 每个格子的像素大小
    public static final int SLOT = 18;
    // 玩家背包列数
    public static final int PLAYER_COLS = 9;
    // 玩家背包行数（主物品栏）
    public static final int PLAYER_ROWS = 3;
    // 热键栏行数
    public static final int HOTBAR_ROWS = 1;

    // ===================== 字段 =====================

    // 容器列数和行数
    protected final int cols, rows;
    // 容器格子总数
    protected final int slotCount;
    // 界面宽高（由布局公式算出）
    private final int cachedWidth, cachedHeight;

    // ===================== 构造器 =====================

    // type:        菜单类型（由注册表提供）
    // containerId: 容器会话 ID
    // cols:        容器列数
    // rows:        容器行数
    // container:   底层物品容器
    // inv:         玩家背包（用于添加玩家槽位）
    protected GenericContainerMenu(MenuType<?> type, int containerId, int cols, int rows, Container container, Inventory inv)
    {
        super(type, containerId);
        this.cols = cols;
        this.rows = rows;
        this.slotCount = cols * rows;
        this.cachedWidth = PAD + PLAYER_COLS * SLOT + PAD;
        this.cachedHeight = PAD + TITLE_H + rows * SLOT + GAP + PLAYER_ROWS * SLOT + HOTBAR_ROWS * SLOT + PAD;
        addContainerSlots(container);
        addPlayerSlots(inv);
    }

    // ===================== 尺寸访问器 =====================

    // 界面总宽度
    public int getImageWidth()
    {
        return cachedWidth;
    }

    // 界面总高度
    public int getImageHeight()
    {
        return cachedHeight;
    }

    // ===================== 布局计算 =====================

    // 容器区域起始 Y 坐标
    public static int containerY()
    {
        return PAD + TITLE_H;
    }

    // 玩家主物品栏起始 Y 坐标（基于容器行数）
    public static int playerY(int rows)
    {
        return containerY() + rows * SLOT + GAP;
    }

    // 热键栏起始 Y 坐标（基于容器行数）
    public static int hotbarY(int rows)
    {
        return playerY(rows) + PLAYER_ROWS * SLOT;
    }

    // ===================== 槽位添加 =====================

    // 添加容器格子。子类可重写此方法使用自定义 Slot 类型。
    // container: 底层物品容器
    protected void addContainerSlots(Container container)
    {
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                addSlot(createContainerSlot(container, c + r * cols,
                        PAD + c * SLOT, containerY() + r * SLOT));
            }
        }
    }

    // 创建单个容器槽位。子类重写此方法可返回自定义 Slot（如支持超大堆叠）。
    // container: 底层物品容器
    // index:     槽位索引
    // x, y:      槽位像素坐标
    protected Slot createContainerSlot(Container container, int index, int x, int y)
    {
        return new Slot(container, index, x, y);
    }

    // 添加玩家背包槽位（主物品栏 + 热键栏）
    // inv: 玩家背包
    protected void addPlayerSlots(Inventory inv)
    {
        // 主物品栏（3行×9列，索引从 9 开始跳过热键栏）
        for (int r = 0; r < PLAYER_ROWS; r++)
        {
            for (int c = 0; c < PLAYER_COLS; c++)
            {
                addSlot(new Slot(inv, c + r * PLAYER_COLS + PLAYER_COLS,
                        PAD + c * SLOT, playerY(rows) + r * SLOT));
            }
        }
        // 热键栏（1行×9列，索引 0-8）
        for (int c = 0; c < PLAYER_COLS; c++)
        {
            addSlot(new Slot(inv, c, PAD + c * SLOT, hotbarY(rows)));
        }
    }

    // ===================== 物品转移 =====================

    // Shift 点击在容器与玩家背包之间转移物品。
    // 子类有特殊槽位分区时可重写此方法。
    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        Slot slot = slots.get(index);
        if (!slot.hasItem())
        {
            return ItemStack.EMPTY;
        }
        ItemStack original = slot.getItem();
        ItemStack copy = original.copy();
        if (index < slotCount)
        {
            // 容器 → 玩家
            if (!moveItemStackTo(original, slotCount, slotCount + FennecLibConfig.PLAYER_INVENTORY_SIZE, true))
            {
                return ItemStack.EMPTY;
            }
        }
        else
        {
            // 玩家 → 容器
            if (!moveItemStackTo(original, 0, slotCount, false))
            {
                return ItemStack.EMPTY;
            }
        }
        if (original.isEmpty())
        {
            slot.set(ItemStack.EMPTY);
        }
        else
        {
            slot.setChanged();
        }
        return copy;
    }

    // ===================== 基类实现 =====================

    @Override
    public boolean stillValid(Player player)
    {
        return true;
    }
}
