package com.fennecmomo.momolib.template;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * 通用双按钮确认弹窗 Screen。
 * 服务端通过 SimpleMenuProvider + ConfirmMenu 打开。
 */
public class ConfirmScreen extends AbstractContainerScreen<ConfirmMenu>
{
    public ConfirmScreen(ConfirmMenu menu, Inventory inv, Component ignored)
    {
        super(menu, inv, Component.empty(), 200, 100);
    }

    @Override
    protected void init()
    {
        super.init();
        int cx = this.leftPos + 100;
        int cy = this.topPos + 55;

        addRenderableWidget(Button.builder(
                Component.literal(menu.leftLabel), btn -> {
                    this.minecraft.player.connection.sendCommand(menu.leftCommand);
                    this.onClose();
                }).pos(cx - 80, cy).size(70, 20).build());

        addRenderableWidget(Button.builder(
                Component.literal(menu.rightLabel), btn -> {
                    this.minecraft.player.connection.sendCommand(menu.rightCommand);
                    this.onClose();
                }).pos(cx + 10, cy).size(70, 20).build());
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor g, int mx, int my)
    {
        // 不画默认标题/物品栏标签
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float pt)
    {
        g.fill(0, 0, this.width, this.height, 0xBB000000);
        g.fill(this.leftPos, this.topPos,
                this.leftPos + this.imageWidth, this.topPos + this.imageHeight, 0xFFC6C6C6);
        super.extractRenderState(g, mx, my, pt);

        int cx = this.leftPos + 100;
        g.centeredText(this.font, Component.literal(menu.title),
                cx, this.topPos + 12, 0xFF404040);
        if (!menu.info.isEmpty())
        {
            g.centeredText(this.font, Component.literal(menu.info),
                    cx, this.topPos + 30, 0xFF555555);
        }
    }
}
