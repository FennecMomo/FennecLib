package com.fennecmomo.fenneclib.template.List;

import java.util.function.Consumer;

import com.fennecmomo.fenneclib.template.Interface.IListItem;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

// 按钮条目——支持三种模式：纯按钮、文本+按钮、纯文本。
// 实现 IListItem 可放入 ContentList。
public class ButtonListItem implements IListItem
{
    // ===================== 字段 =====================

    // 纯按钮模式
    private final Button button;
    // 文本+按钮模式（FrameLayout 水平布局）
    private final FrameLayout row;
    private final StringWidget label;
    // 按钮宽度和条目高度（缓存在 setEntryWidth 时重布局）
    private final int btnW, itemH;

    // ===================== 构造器 =====================

    // 纯按钮模式。
    // x, y:    按钮位置
    // w, h:    按钮尺寸
    // label:   按钮文字
    // onPress: 点击回调
    // adder:   widget 注册回调
    public ButtonListItem(int x, int y, int w, int h, Component label, Button.OnPress onPress, Consumer<AbstractWidget> adder)
    {
        button = Button.builder(label, onPress).pos(x, y).size(w, h).build();
        adder.accept(button);
        this.row = null;
        this.label = null;
        this.btnW = 0;
        this.itemH = 0;
    }

    // 文本+按钮模式。
    // x, y:     条目位置
    // w, h:     条目尺寸
    // font:     字体
    // text:     左侧显示的文字
    // btnLabel: 按钮文字
    // btnW, btnH: 按钮尺寸
    // onPress:  按钮点击回调
    // adder:    widget 注册回调
    public ButtonListItem(int x, int y, int w, int h, Font font, Component text, String btnLabel, int btnW, int btnH, Button.OnPress onPress, Consumer<AbstractWidget> adder)
    {
        this.btnW = btnW;
        this.itemH = h;
        this.button = null;
        row = new FrameLayout(w, h);
        row.setPosition(x, y);
        label = new StringWidget(text.copy(), font);
        row.addChild(label, LayoutSettings.defaults().align(0f, 0.5f));
        Button btn = Button.builder(Component.literal(btnLabel), onPress).size(btnW, btnH).build();
        row.addChild(btn, LayoutSettings.defaults().align(1f, 0.5f));
        row.arrangeElements();
        row.visitWidgets(adder);
    }

    // 纯文本模式（无按钮）。
    // x, y:  条目位置
    // w, h:  条目尺寸
    // font:  字体
    // text:  左侧显示的文字
    // adder: widget 注册回调
    public ButtonListItem(int x, int y, int w, int h, Font font, Component text, Consumer<AbstractWidget> adder)
    {
        this.btnW = 0;
        this.itemH = h;
        this.button = null;
        row = new FrameLayout(w, h);
        row.setPosition(x, y);
        label = new StringWidget(text.copy(), font);
        row.addChild(label, LayoutSettings.defaults().align(0f, 0.5f));
        row.arrangeElements();
        row.visitWidgets(adder);
    }

    // ===================== 公开方法 =====================

    // 修改显示文本（仅文本+按钮/纯文本模式）
    public void setText(Component t)
    {
        if (label != null)
        {
            label.setMessage(t);
        }
    }

    // 修改文本颜色（仅文本+按钮/纯文本模式）
    public void setTextColor(int c)
    {
        if (label != null)
        {
            label.setMessage(label.getMessage().copy().withStyle(Style.EMPTY.withColor(c & 0xFFFFFF)));
        }
    }

    // ===================== IListItem 实现 =====================
    @Override
    public void setEntryWidth(int w)
    {
        if (button != null)
        {
            button.setWidth(w);
        }
        else if (row != null)
        {
            row.setMinDimensions(w, itemH);
            row.arrangeElements();
        }
    }

    @Override
    public void setEntryPosition(int x, int y)
    {
        if (button != null)
        {
            button.setPosition(x, y);
        }
        else if (row != null)
        {
            row.setPosition(x, y);
        }
    }

    @Override
    public void activate()
    {
        if (button != null)
        {
            button.visible = true;
        }
        else if (row != null)
        {
            label.visible = true;
        }
    }

    @Override
    public void deactivate()
    {
        if (button != null)
        {
            button.visible = false;
        }
        else if (row != null)
        {
            label.visible = false;
        }
    }
}
