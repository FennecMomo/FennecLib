package com.fennecmomo.momolib.template;

// 进度条渲染器——把 0~1 的值画成彩色条
// W_StatBar 组合此接口来渲染进度条部分
@FunctionalInterface
public interface R_Bar
{
    // x,y,w,h：进度条区域的像素位置
    // value：0.0~1.0 的当前值
    void draw(net.minecraft.client.gui.GuiGraphicsExtractor g, int x, int y, int w, int h, float value);

    // 默认渲染器：灰色底色 + 彩色前景
    // 高值绿色(>50%)、中值橙色(>20%)、低值红色
    R_Bar DEFAULT = (g, x, y, w, h, v) ->
    {
        int f = Math.round(v * w);
        int c = v > 0.5f ? 0xFF55FF55 : v > 0.2f ? 0xFFAA5500 : 0xFFFF5555;
        g.fill(x, y + 3, x + w, y + h - 3, 0xFF333333);
        g.fill(x, y + 3, x + f, y + h - 3, c);
    };
}
