package com.fennecmomo.momolib.template.Interface;

import com.fennecmomo.momolib.MomoLibConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;

// 进度条渲染器——把 0~1 的值画成彩色条。
// StateBar 组合此接口来渲染进度条部分。
@FunctionalInterface
public interface IBar
{
    // ===================== 渲染方法 =====================

    // 绘制进度条。g: 图形提取器, x/y/w/h: 进度条区域的像素位置, value: 0.0~1.0 的当前值
    void draw(GuiGraphicsExtractor g, int x, int y, int w, int h, float value);

    // ===================== 默认渲染器 =====================
    // 默认渲染器：灰色底色 + 彩色前景。
    // 高值绿色(>50%)、中值橙色(>20%)、低值红色
    IBar DEFAULT = (g, x, y, w, h, v) ->
    {
        int f = Math.round(v * w);
        int c = v > MomoLibConfig.BAR_COLOR_GREEN_THRESHOLD ? 0xFF55FF55
                : v > MomoLibConfig.BAR_COLOR_ORANGE_THRESHOLD ? 0xFFAA5500 : 0xFFFF5555;
        int pad = MomoLibConfig.BAR_VERTICAL_PADDING;
        g.fill(x, y + pad, x + w, y + h - pad, 0xFF333333);
        g.fill(x, y + pad, x + f, y + h - pad, c);
    };
}
