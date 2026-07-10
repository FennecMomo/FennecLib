package com.fennecmomo.momolib.template;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import java.util.function.Consumer;

// 通用状态条组件——显示"标签 + 数值 + 进度条"三栏
// 实现 I_Entry 可放入 W_EntryList
// 用于居民信息界面的饥饿/休息/心情/健康显示，以及技能经验条
public class W_StatBar extends AbstractWidget implements I_Entry
{
    // 数值格式
    public enum Fmt
    {
        RATIO,    // 百分比显示：50%
        INTEGER,  // 整数比显示：50/100
        EXP       // 经验值显示：当前/升级所需
    }

    // I_Entry 接口实现：宽度/位置/显隐
    @Override public void setEntryWidth(int w) { setWidth(w); }
    @Override public void setEntryPosition(int x, int y) { setPosition(x, y); }
    @Override public void activate() { this.visible = true; }
    @Override public void deactivate() { this.visible = false; }

    private final net.minecraft.client.gui.Font font;
    private String label;
    private float value, max = 100f;
    private Fmt fmt = Fmt.RATIO;
    private int labelColor = 0xFFFFFFFF, valueColor = 0xFFAAAAAA;
    private R_Bar barRenderer = R_Bar.DEFAULT;
    // null = 自动生成数值文本，非 null = 直接显示
    private String valOverride;

    // label：状态名称
    // value：当前值
    // adder：widget注册回调
    public W_StatBar(int x, int y, int w, int h, net.minecraft.client.gui.Font font,
                     String label, float value, Consumer<AbstractWidget> adder)
    {
        super(x, y, w, h, Component.empty());
        this.font = font; this.label = label; this.value = value;
        adder.accept(this);
    }

    // 链式配置方法
    // 设置标签文字颜色
    public W_StatBar withLabelColor(int c) { labelColor = c; return this; }
    // 设置数值文字颜色
    public W_StatBar withValueColor(int c) { valueColor = c; return this; }
    // 设置自定义进度条渲染器
    public W_StatBar withBarRenderer(R_Bar r) { barRenderer = r; return this; }
    // 设置最大值（默认100）
    public W_StatBar withMax(float m) { max = m; return this; }
    // 设置数值格式
    public W_StatBar withFmt(Fmt f) { fmt = f; return this; }
    // 覆盖数值文本（如显示"Lv.5"而非百分比）
    public W_StatBar withValText(String t) { valOverride = t; return this; }
    public void setValue(float v) { value = v; }

    // 渲染三栏布局：标签 | 数值 | 进度条
    // 宽度分配：标签2/9 + 数值2/9 + 间距1/9 + 进度条4/9
    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor g, int mx, int my, float pt)
    {
        int x = getX(), y = getY(), w = getWidth(), h = getHeight();
        int lw = w * 2 / 9, vw = w * 2 / 9, sw = w / 9, bw = w - lw - vw - sw;

        // 标签
        g.text(font, Component.literal(label).withStyle(Style.EMPTY.withColor(labelColor & 0xFFFFFF)),
                x, y, labelColor, false);

        // 数值文本
        String vs = valOverride != null ? valOverride : switch (fmt)
        {
            case RATIO -> String.format("%.0f%%", value / max * 100);
            case INTEGER -> String.format("%.0f/%.0f", value, max);
            case EXP -> String.format("%.0f/%.0f", value, max);
        };
        g.text(font, Component.literal(vs).withStyle(Style.EMPTY.withColor(valueColor & 0xFFFFFF)),
                x + lw, y, valueColor, false);

        // 进度条
        barRenderer.draw(g, x + lw + vw + sw, y, bw, h, Math.clamp(value / max, 0f, 1f));
    }

    @Override protected void updateWidgetNarration(NarrationElementOutput o) {}
    @Override public boolean mouseClicked(MouseButtonEvent e, boolean d) { return false; }
}
