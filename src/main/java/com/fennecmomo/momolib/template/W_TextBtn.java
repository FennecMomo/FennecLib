package com.fennecmomo.momolib.template;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import java.util.function.Consumer;

// 文字按钮条目——左侧显示文本，右侧可选一个操作按钮（如"×"取消按钮）
// 实现 I_Entry 可放入 W_EntryList
// 支持两种模式：纯文本（无按钮）和 文本+按钮
public class W_TextBtn implements I_Entry
{
    // FrameLayout 水平布局：文本左对齐 + 按钮右对齐
    private final FrameLayout row;
    // 文本标签
    private final StringWidget label;
    // 操作按钮（null = 纯文本模式）
    private final Button button;
    // 按钮宽度和条目高度（缓存在 setEntryWidth 时重布局）
    private final int btnW, itemH;

    // 文本+按钮模式
    // text：左侧显示的文字
    // btnLabel：按钮文字
    // btnW, btnH：按钮尺寸
    // onPress：按钮点击回调
    public W_TextBtn(int x, int y, int w, int h, net.minecraft.client.gui.Font font,
                     Component text, String btnLabel, int btnW, int btnH,
                     Button.OnPress onPress, Consumer<AbstractWidget> adder)
    {
        this.btnW = btnW; this.itemH = h;
        row = new FrameLayout(w, h); row.setPosition(x, y);
        label = new StringWidget(text.copy(), font);
        row.addChild(label, LayoutSettings.defaults().align(0f, 0.5f));
        button = Button.builder(Component.literal(btnLabel), onPress).size(btnW, btnH).build();
        row.addChild(button, LayoutSettings.defaults().align(1f, 0.5f));
        row.arrangeElements(); row.visitWidgets(adder);
    }

    // 纯文本模式（无按钮）
    public W_TextBtn(int x, int y, int w, int h, net.minecraft.client.gui.Font font,
                     Component text, Consumer<AbstractWidget> adder)
    {
        this.btnW = 0; this.itemH = h;
        row = new FrameLayout(w, h); row.setPosition(x, y);
        label = new StringWidget(text.copy(), font);
        row.addChild(label, LayoutSettings.defaults().align(0f, 0.5f));
        button = null;
        row.arrangeElements(); row.visitWidgets(adder);
    }

    // 修改显示文本
    public void setText(Component t) { label.setMessage(t); }
    // 修改文本颜色
    public void setTextColor(int c) { label.setMessage(label.getMessage().copy().withStyle(Style.EMPTY.withColor(c & 0xFFFFFF))); }

    @Override public void setEntryWidth(int w) { row.setMinDimensions(w, itemH); row.arrangeElements(); }
    @Override public void setEntryPosition(int x, int y) { row.setPosition(x, y); }
    @Override public void activate() { label.visible = true; if (button != null) button.visible = true; }
    @Override public void deactivate() { label.visible = false; if (button != null) button.visible = false; }
}
