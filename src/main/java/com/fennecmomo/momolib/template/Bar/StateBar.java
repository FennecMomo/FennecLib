package com.fennecmomo.momolib.template.Bar;

import java.util.function.Consumer;

import com.fennecmomo.momolib.MomoLibConfig;
import com.fennecmomo.momolib.template.Interface.IBar;
import com.fennecmomo.momolib.template.Interface.IListItem;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

// 通用状态条组件——显示"标签 + 数值 + 进度条"三栏。
// 实现 IListItem 可放入 ContentList。
// 用于居民信息界面的饥饿/休息/心情/健康显示，以及技能经验条。
public class StateBar extends AbstractWidget implements IListItem
{
    // ===================== 内部类型 =====================

    // 数值显示格式
    public enum Fmt
    {
        RATIO,    // 百分比显示：50%
        INTEGER,  // 整数比显示：50/100
        EXP       // 经验值显示：当前/升级所需
    }

    // ===================== 字段 =====================
    // 字体（用于绘制标签和数值文本）
    private final Font font;
    // 状态名称
    private String label;
    // 当前值和最大值
    private float value, max = MomoLibConfig.BAR_DEFAULT_MAX;
    // 数值显示格式
    private Fmt fmt = Fmt.RATIO;
    // 标签和数值的文字颜色
    private int labelColor = 0xFFFFFFFF, valueColor = 0xFFAAAAAA;
    // 进度条渲染器（可自定义）
    private IBar barRenderer = IBar.DEFAULT;
    // null = 自动生成数值文本，非 null = 直接显示该字符串
    private String valOverride;

    // ===================== IListItem 接口实现 =====================
    @Override
    public void setEntryWidth(int w)
    {
        setWidth(w);
    }

    @Override
    public void setEntryPosition(int x, int y)
    {
        setPosition(x, y);
    }

    @Override
    public void activate()
    {
        this.visible = true;
    }

    @Override
    public void deactivate()
    {
        this.visible = false;
    }

    // ===================== 构造器 =====================
    // 创建状态条组件。
    // x, y, w, h: 组件的像素位置
    // font:  字体（用于绘制文本）
    // label: 状态名称
    // value: 当前值
    // adder: widget 注册回调
    public StateBar(int x, int y, int w, int h, Font font, String label, float value, Consumer<AbstractWidget> adder)
    {
        super(x, y, w, h, Component.empty());
        this.font = font;
        this.label = label;
        this.value = value;
        adder.accept(this);
    }

    // ===================== 链式配置 =====================
    // 设置标签文字颜色
    public StateBar withLabelColor(int c)
    {
        labelColor = c;
        return this;
    }

    // 设置数值文字颜色
    public StateBar withValueColor(int c)
    {
        valueColor = c;
        return this;
    }

    // 设置自定义进度条渲染器
    public StateBar withBarRenderer(IBar r)
    {
        barRenderer = r;
        return this;
    }

    // 设置最大值（默认 100）
    public StateBar withMax(float m)
    {
        max = m;
        return this;
    }

    // 设置数值格式
    public StateBar withFmt(Fmt f)
    {
        fmt = f;
        return this;
    }

    // 覆盖数值文本（如显示"Lv.5"而非百分比）
    public StateBar withValText(String t)
    {
        valOverride = t;
        return this;
    }

    // 设置当前值
    public void setValue(float v)
    {
        value = v;
    }

    // ===================== 渲染 =====================
    // 绘制三栏布局：标签 | 数值 | 进度条。
    // 宽度分配：标签 2/9 + 数值 2/9 + 间距 1/9 + 进度条 4/9
    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor g, int mx, int my, float pt)
    {
        int x = getX(), y = getY(), w = getWidth(), h = getHeight();
        int lw = w * MomoLibConfig.BAR_LABEL_RATIO / MomoLibConfig.BAR_TOTAL_RATIO;
        int vw = w * MomoLibConfig.BAR_VALUE_RATIO / MomoLibConfig.BAR_TOTAL_RATIO;
        int sw = w * MomoLibConfig.BAR_GAP_RATIO / MomoLibConfig.BAR_TOTAL_RATIO;
        int bw = w - lw - vw - sw;

        // 标签
        g.text(font, Component.literal(label).withStyle(Style.EMPTY.withColor(labelColor & 0xFFFFFF)),
                x, y, labelColor, false);

        // 数值文本
        String vs = valOverride != null ? valOverride : switch (fmt)
        {
            case RATIO -> String.format("%.0f%%", value / max * MomoLibConfig.BAR_PERCENT_MULTIPLIER);
            case INTEGER -> String.format("%.0f/%.0f", value, max);
            case EXP -> String.format("%.0f/%.0f", value, max);
        };
        g.text(font, Component.literal(vs).withStyle(Style.EMPTY.withColor(valueColor & 0xFFFFFF)),
                x + lw, y, valueColor, false);

        // 进度条
        barRenderer.draw(g, x + lw + vw + sw, y, bw, h, Math.clamp(value / max, 0f, 1f));
    }

    // ===================== 基类实现 =====================
    // 无障碍叙述（状态条无需）
    @Override
    protected void updateWidgetNarration(NarrationElementOutput o)
    {
    }

    // 状态条不响应鼠标点击
    @Override
    public boolean mouseClicked(MouseButtonEvent e, boolean d)
    {
        return false;
    }
}
