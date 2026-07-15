package com.fennecmomo.momolib.template.UI;

import com.fennecmomo.momolib.template.Data.GenericContainerMenu;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;

// 通用容器屏幕基类——配合 GenericContainerMenu 使用。
// 内置蒙版、灰色面板、边框和标题渲染，子类只需关注自定义逻辑。
public class GenericContainerScreen<T extends GenericContainerMenu> extends AbstractContainerScreen<T>
{
    // ===================== 构造器 =====================

    // menu:  服务端传入的菜单（由 GenericContainerMenu 子类提供）
    // inv:   玩家背包
    // title: 屏幕标题
    public GenericContainerScreen(T menu, Inventory inv, Component title)
    {
        super(menu, inv, title, menu.getImageWidth(), menu.getImageHeight());
    }

    // ===================== 初始化 =====================

    // 注册标题 Widget，跳过原版默认标签
    @Override
    protected void init()
    {
        super.init();
        LinearLayout bar = LinearLayout.horizontal();
        bar.addChild(new StringWidget(
                getTitle().copy().withStyle(Style.EMPTY.withColor(0xFFFFFF)),
                this.font));
        bar.arrangeElements();
        bar.setPosition(this.leftPos + GenericContainerMenu.PAD,
                this.topPos + GenericContainerMenu.PAD);
        bar.visitWidgets(this::addRenderableWidget);
    }

    // ===================== 渲染 =====================

    // 绘制全屏蒙版 + 灰色面板 + 边框，然后调用基类渲染槽位和物品
    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick)
    {
        g.fill(0, 0, this.width, this.height, 0xBB000000);
        g.fill(this.leftPos, this.topPos,
                this.leftPos + this.imageWidth, this.topPos + this.imageHeight, 0xFFC6C6C6);
        g.outline(this.leftPos, this.topPos, this.imageWidth, this.imageHeight, 0xFF555555);
        super.extractRenderState(g, mouseX, mouseY, partialTick);
    }

    // 禁止原版默认标题和背包标签（由 init 中的 StringWidget 替代）
    @Override
    protected void extractLabels(GuiGraphicsExtractor g, int mouseX, int mouseY)
    {
    }
}
