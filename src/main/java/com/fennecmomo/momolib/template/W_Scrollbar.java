package com.fennecmomo.momolib.template;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import java.util.function.IntConsumer;

// 通用滚动条组件——继承 AbstractWidget 直接参与 GUI 渲染
// 支持鼠标拖拽滑块和滚轮滚动
// 滚动变化通过 IntConsumer 回调通知外部
public class W_Scrollbar extends AbstractWidget
{
    // 滚动回调：参数为新的滚动位置
    private final IntConsumer onScroll;
    // maxScroll：最大滚动位置（条目数-可见行数）
    private int maxScroll, current;
    // 是否正在拖拽滑块
    private boolean dragging;
    // 可见行数，用于计算滑块大小
    private final int visibleRows;

    // x,y,w,h：滚动条的像素位置
    // visibleRows：列表框同时可见的行数
    // onScroll：滚动位置变化回调
    public W_Scrollbar(int x, int y, int w, int h, int visibleRows, IntConsumer onScroll)
    {
        super(x, y, w, h, Component.empty());
        this.visibleRows = visibleRows; this.onScroll = onScroll;
    }

    // 设置最大滚动值并钳制当前位置
    public void setMaxScroll(int m) { maxScroll = m; if (current > m) current = m; }

    // 滚轮事件：鼠标在列表区域（滚动条左侧152px范围内也算）时响应
    @Override public boolean mouseScrolled(double mx, double my, double sx, double sy)
    {
        if (!isMouseOver(mx, my) && !(mx >= getX() - 152 && mx <= getX() && my >= getY() && my <= getY() + height)) return false;
        setCurrent(current - (int) Math.signum(sy)); return true;
    }

    // 鼠标按下开始拖拽
    @Override public boolean mouseClicked(MouseButtonEvent e, boolean d)
    {
        if (e.button() == 0 && isMouseOver(e.x(), e.y())) { dragging = true; updateFromMouse(e.y()); return true; }
        return false;
    }

    @Override public boolean mouseReleased(MouseButtonEvent e) { if (e.button() == 0) dragging = false; return false; }

    // 拖拽中持续更新位置
    @Override public boolean mouseDragged(MouseButtonEvent e, double dx, double dy) { if (dragging) updateFromMouse(e.y()); return dragging; }

    // 根据鼠标Y坐标计算滑块位置
    // 鼠标在轨道上时，滑块中心对齐鼠标
    private void updateFromMouse(double my)
    {
        if (maxScroll <= 0) return;
        int th = height - 2, thumbH = thumbHeight(th);
        float r = (float)(my - getY() - 1 - thumbH / 2f) / (th - thumbH);
        setCurrent((int)(r * maxScroll + 0.5f));
    }

    // 设置当前滚动位置并触发回调
    private void setCurrent(int v) { current = Math.clamp(v, 0, maxScroll); onScroll.accept(current); }

    // 渲染：灰色轨道 + 灰色滑块（拖拽中高亮为浅灰）
    @Override protected void extractWidgetRenderState(GuiGraphicsExtractor g, int mx, int my, float pt)
    {
        if (maxScroll <= 0) return;
        int x = getX(), y = getY(), h = height;
        g.fill(x, y, x + width, y + h, 0xFF333333);
        int th = h - 2, thumbH = thumbHeight(th), thumbY = y + 1;
        if (maxScroll > 0) thumbY += (int)((th - thumbH) * (float)current / maxScroll);
        g.fill(x + 1, thumbY, x + width - 1, thumbY + thumbH, dragging ? 0xFFBBBBBB : 0xFF888888);
    }

    // 计算滑块高度：占比 = 可见行 / 总行数
    private int thumbHeight(int th) { return Math.max(8, th * visibleRows / (maxScroll + visibleRows)); }

    @Override protected void updateWidgetNarration(NarrationElementOutput o) {}
}
