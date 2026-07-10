package com.fennecmomo.momolib.template;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import java.util.function.Consumer;

// 按钮条目——把原版 Button 包装成 I_Entry，使其能放入 W_EntryList
// 通过 activate/deactivate 控制显隐，setEntryPosition 控制位置
public class W_BtnEntry implements I_Entry
{
    // 被包装的原版按钮
    private final Button button;

    // label：按钮文字
    // onPress：点击回调
    // adder：widget 注册回调
    public W_BtnEntry(int x, int y, int w, int h, Component label,
                      Button.OnPress onPress, Consumer<AbstractWidget> adder)
    {
        button = Button.builder(label, onPress).pos(x, y).size(w, h).build();
        adder.accept(button);
    }

    // 调整按钮宽度
    @Override public void setEntryWidth(int w) { button.setWidth(w); }
    // 移动按钮到指定位置
    @Override public void setEntryPosition(int x, int y) { button.setPosition(x, y); }
    // 显示按钮
    @Override public void activate() { button.visible = true; }
    // 隐藏按钮
    @Override public void deactivate() { button.visible = false; }
}
