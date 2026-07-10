package com.fennecmomo.momolib.template;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.Slot;
import java.util.function.Consumer;

// 滚动容器背景渲染工具类
// 为 AbstractContainerScreen 提供统一的背景和标题渲染
public final class S_Container
{
    // 原版物品栏格子贴图
    private static final Identifier SLOT = Identifier.withDefaultNamespace("container/slot");

    // 私有构造器——纯工具类不能实例化
    private S_Container() {}

    // 渲染容器背景
    // s：当前屏幕实例
    // l,t,w,h：容器区域的左上角坐标和宽高
    // 绘制半透明黑色蒙版 + 灰色容器背景 + 所有物品栏格子
    public static void renderBg(AbstractContainerScreen<?> s, GuiGraphicsExtractor g,
                                int l, int t, int w, int h)
    {
        g.fill(0, 0, s.width, s.height, 0xBB000000);
        g.fill(l, t, l + w, t + h, 0xFFC6C6C6);
        g.outline(l, t, w, h, 0xFF555555);
        for (Slot slot : s.getMenu().slots)
            g.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT, l + slot.x - 1, t + slot.y - 1, 18, 18);
    }

    // 初始化容器标题栏
    // 创建一个水平布局的 StringWidget 显示容器名称
    public static void initTitle(AbstractContainerScreen<?> s, int l, int t,
                                  net.minecraft.client.gui.Font f,
                                  Consumer<net.minecraft.client.gui.components.AbstractWidget> a)
    {
        LinearLayout bar = LinearLayout.horizontal();
        bar.addChild(new StringWidget(s.getTitle().copy().withStyle(Style.EMPTY.withColor(0xFFFFFF)), f));
        bar.arrangeElements();
        bar.setPosition(l + C_Container.PAD, t + C_Container.PAD);
        bar.visitWidgets(a);
    }
}
