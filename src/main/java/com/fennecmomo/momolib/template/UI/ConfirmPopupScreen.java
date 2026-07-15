package com.fennecmomo.momolib.template.UI;

import com.fennecmomo.momolib.template.Data.ConfirmPopupMenu;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

// 通用双按钮确认弹窗的屏幕（客户端渲染）。
// 服务端通过 SimpleMenuProvider + ConfirmPopupMenu 打开此界面。
// 渲染：半透明黑色蒙版 + 灰色对话框 + 标题/信息文本 + 左右两个按钮。
public class ConfirmPopupScreen extends AbstractContainerScreen<ConfirmPopupMenu>
{
    // ===================== 构造器 =====================

    // menu:    服务端传入的菜单数据
    // inv:     玩家背包
    // ignored: 标题参数（不使用，由 menu.title 提供）
    public ConfirmPopupScreen(ConfirmPopupMenu menu, Inventory inv, Component ignored)
    {
        super(menu, inv, Component.empty(), 200, 100);
    }

    // ===================== 初始化 =====================
    // 创建左右两个按钮并注册到界面，点击后发送对应命令并关闭。
    @Override
    protected void init()
    {
        super.init();
        int cx = this.leftPos + 100;
        int cy = this.topPos + 55;

        addRenderableWidget(Button.builder(
                Component.literal(menu.leftLabel), btn ->
                {
                    this.minecraft.player.connection.sendCommand(menu.leftCommand);
                    this.onClose();
                }).pos(cx - 80, cy).size(70, 20).build());

        addRenderableWidget(Button.builder(
                Component.literal(menu.rightLabel), btn ->
                {
                    this.minecraft.player.connection.sendCommand(menu.rightCommand);
                    this.onClose();
                }).pos(cx + 10, cy).size(70, 20).build());
    }

    // ===================== 渲染 =====================
    // 不绘制默认标题和物品栏标签。
    @Override
    protected void extractLabels(GuiGraphicsExtractor g, int mx, int my)
    {
    }

    // 绘制蒙版、对话框背景、标题和详细信息文本。
    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float pt)
    {
        // 半透明黑色蒙版覆盖全屏
        g.fill(0, 0, this.width, this.height, 0xBB000000);
        // 灰色对话框背景
        g.fill(this.leftPos, this.topPos, this.leftPos + this.imageWidth, this.topPos + this.imageHeight, 0xFFC6C6C6);
        super.extractRenderState(g, mx, my, pt);

        int cx = this.leftPos + 100;
        // 标题文本
        g.centeredText(this.font, Component.literal(menu.title), cx, this.topPos + 12, 0xFF404040);
        // 详细信息文本（非空时显示）
        if (!menu.info.isEmpty())
        {
            g.centeredText(this.font, Component.literal(menu.info), cx, this.topPos + 30, 0xFF555555);
        }
    }
}
