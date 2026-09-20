package com.fennecmomo.fenneclib.template.List;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.fennecmomo.fenneclib.template.Bar.ScrollBar;
import com.fennecmomo.fenneclib.template.Interface.IListItem;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.input.MouseButtonEvent;

// 可滚动的条目列表——管理一组 IListItem 的显示和滚动。
// 配合 ScrollBar 实现虚拟滚动：只渲染可见行，超出部分通过滚动条访问。
public class ContentList
{
    // ===================== 字段 =====================
    // 列表的屏幕位置和尺寸
    private final int x, y, width, visibleRows, rowHeight;
    // 滚动条组件
    private final ScrollBar scrollbar;
    // 所有条目
    private final List<IListItem> entries = new ArrayList<>();
    // 当前滚动偏移（从第几行开始显示）
    private int scrollOffset;

    // ===================== 构造器 =====================
    // 创建可滚动条目列表并注册滚动条。
    // adder:       widget 注册回调（通过 screen::addRenderableWidget 传入）
    // x, y, w, h: 列表区域
    // rowH:        每行像素高度
    public ContentList(Consumer<AbstractWidget> adder, int x, int y, int w, int h, int rowH)
    {
        this.x = x;
        this.y = y;
        this.width = w;
        this.rowHeight = rowH;
        this.visibleRows = h / rowH;
        this.scrollbar = new ScrollBar(x + w - 6, y, 8, h, visibleRows, v ->
        {
            scrollOffset = v;
            refresh();
        });
        adder.accept(scrollbar);
    }

    // ===================== 条目管理 =====================
    // 设置条目列表。停用旧条目，激活新条目中可见的部分。
    // items: 新的条目列表
    public void setEntries(List<IListItem> items)
    {
        for (var e : entries)
        {
            e.deactivate();
        }
        entries.clear();
        entries.addAll(items);
        int entryW = width - 10;
        for (var item : items)
        {
            item.setEntryWidth(entryW);
        }
        scrollbar.setMaxScroll(Math.max(0, entries.size() - visibleRows));
        if (scrollOffset > entries.size() - visibleRows)
        {
            scrollOffset = Math.max(0, entries.size() - visibleRows);
        }
        refresh();
    }

    // 刷新可见条目：根据 scrollOffset 激活对应范围的条目，其余停用
    public void refresh()
    {
        for (int i = 0; i < entries.size(); i++)
        {
            var w = entries.get(i);
            if (i >= scrollOffset && i < scrollOffset + visibleRows)
            {
                w.setEntryPosition(x, y + (i - scrollOffset) * rowHeight);
                w.activate();
            }
            else
            {
                w.deactivate();
            }
        }
    }

    // ===================== 事件转发 =====================
    // 鼠标拖拽事件转发给滚动条
    public boolean mouseDragged(MouseButtonEvent e, double dx, double dy)
    {
        return scrollbar.mouseDragged(e, dx, dy);
    }

    // 滚轮事件转发给滚动条
    public boolean mouseScrolled(double mx, double my, double sx, double sy)
    {
        return scrollbar.mouseScrolled(mx, my, sx, sy);
    }

    // 获取当前滚动偏移量
    public int getScrollOffset()
    {
        return scrollOffset;
    }
}
