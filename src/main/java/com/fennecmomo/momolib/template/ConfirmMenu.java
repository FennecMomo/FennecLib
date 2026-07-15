package com.fennecmomo.momolib.template;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

// 通用确认/双按钮弹窗的 Menu（服务端数据容器）。
// 存储标题、信息文本、左右按钮的标签和对应命令。
// 客户端通过 ConfirmScreen 渲染，服务端通过 SimpleMenuProvider 打开。
public class ConfirmMenu extends AbstractContainerMenu {
    // ===================== 字段 =====================

    // 弹窗标题
    public final String title;
    // 弹窗详细信息（可选，空字符串表示不显示）
    public final String info;
    // 左侧按钮标签文本
    public final String leftLabel;
    // 左侧按钮点击后发送的命令
    public final String leftCommand;
    // 右侧按钮标签文本
    public final String rightLabel;
    // 右侧按钮点击后发送的命令
    public final String rightCommand;

    // ===================== 构造器 =====================
    // 从网络缓冲区反序列化构造（客户端接收时使用）。
    // containerId: 容器唯一 ID
    // inv:         玩家背包
    // buf:         网络数据缓冲区，按顺序读取 6 个 UTF 字符串
    public ConfirmMenu(int containerId, Inventory inv, RegistryFriendlyByteBuf buf) {
        this(containerId, inv, buf.readUtf(), buf.readUtf(),
                buf.readUtf(), buf.readUtf(), buf.readUtf(), buf.readUtf());
    }

    // 完整参数构造。
    // containerId: 容器唯一 ID
    // inv:         玩家背包
    // title:       弹窗标题
    // info:        弹窗详细信息
    // leftLabel:   左侧按钮标签
    // leftCommand: 左侧按钮命令
    // rightLabel:  右侧按钮标签
    // rightCommand: 右侧按钮命令
    public ConfirmMenu(int containerId, Inventory inv,
            String title, String info,
            String leftLabel, String leftCommand,
            String rightLabel, String rightCommand) {
        super(TemplateRegistration.CONFIRM_MENU.get(), containerId);
        this.title = title;
        this.info = info;
        this.leftLabel = leftLabel;
        this.leftCommand = leftCommand;
        this.rightLabel = rightLabel;
        this.rightCommand = rightCommand;
    }

    // ===================== 基类实现 =====================
    // Shift 点击转移物品（确认弹窗无物品栏，直接返回空）。
    @Override
    public ItemStack quickMoveStack(Player p, int i) {
        return ItemStack.EMPTY;
    }

    // 判断菜单是否仍然有效（确认弹窗始终有效）。
    @Override
    public boolean stillValid(Player p) {
        return true;
    }
}
