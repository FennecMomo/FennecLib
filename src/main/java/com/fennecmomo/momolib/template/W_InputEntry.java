package com.fennecmomo.momolib.template;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import java.util.function.Consumer;
import java.util.function.Predicate;

// 输入框条目——把原版 EditBox 包装成 I_Entry，使其能放入 W_EntryList
// 用于任务发布界面的数量输入框
public class W_InputEntry implements I_Entry
{
    // 被包装的原版输入框
    private final EditBox input;

    // font：字体
    // hint：占位提示文字
    // adder：widget注册回调
    public W_InputEntry(int x, int y, int w, int h,
                        net.minecraft.client.gui.Font font, Component hint,
                        Consumer<AbstractWidget> adder)
    {
        input = new EditBox(font, x, y, w, h, hint);
        adder.accept(input);
    }

    // 获取底层 EditBox（用于设置值和读取输入）
    public EditBox getInput() { return input; }
    // 设置输入过滤器（如只允许数字：s -> s.matches("\\d*")）
    public void setFilter(Predicate<String> f) { input.setFilter(f); }
    // 设置最大输入长度
    public void setMaxLength(int n) { input.setMaxLength(n); }

    @Override public void setEntryWidth(int w) { input.setWidth(w); }
    @Override public void setEntryPosition(int x, int y) { input.setPosition(x, y); }
    @Override public void activate() { input.visible = true; }
    @Override public void deactivate() { input.visible = false; }
}
