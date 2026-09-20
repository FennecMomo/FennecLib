package com.fennecmomo.fenneclib.template.List;

import java.util.function.Consumer;
import java.util.function.Predicate;

import com.fennecmomo.fenneclib.template.Interface.IListItem;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

// 输入框条目——把原版 EditBox 包装成 IListItem，使其能放入 ContentList。
// 用于任务发布界面的数量输入框。
public class InputListItem implements IListItem
{
    // ===================== 字段 =====================

    // 被包装的原版输入框
    private final EditBox input;

    // ===================== 构造器 =====================
    // 创建输入框条目并注册到界面。
    // x, y, w, h: 输入框位置和尺寸
    // font:        字体
    // hint:        占位提示文字
    // adder:       widget 注册回调
    public InputListItem(int x, int y, int w, int h, Font font, Component hint, Consumer<AbstractWidget> adder)
    {
        input = new EditBox(font, x, y, w, h, hint);
        adder.accept(input);
    }

    // ===================== 配置方法 =====================
    public EditBox getInput()
    {
        return input;
    }

    // 设置输入过滤器（如只允许数字：s -> s.matches("\\d*")）
    public void setFilter(Predicate<String> f)
    {
        input.setFilter(f);
    }

    // 设置最大输入长度
    public void setMaxLength(int n)
    {
        input.setMaxLength(n);
    }

    // ===================== IListItem 实现 =====================
    @Override
    public void setEntryWidth(int w)
    {
        input.setWidth(w);
    }

    @Override
    public void setEntryPosition(int x, int y)
    {
        input.setPosition(x, y);
    }

    @Override
    public void activate()
    {
        input.visible = true;
    }

    @Override
    public void deactivate()
    {
        input.visible = false;
    }
}
