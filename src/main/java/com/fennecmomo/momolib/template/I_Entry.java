package com.fennecmomo.momolib.template;

// 滚动列表条目的统一接口
// 所有可放入 W_EntryList 的组件（按钮、输入框、文本、进度条）都要实现此接口
// 列表通过 activate/deactivate 控制条目可见性，通过 setEntryPosition 控制位置
public interface I_Entry
{
    // 设置条目宽度（列表刷新时调用）
    void setEntryWidth(int w);
    // 设置条目在屏幕上的像素位置
    void setEntryPosition(int x, int y);
    // 激活（显示）条目
    void activate();
    // 停用（隐藏）条目
    void deactivate();
}
