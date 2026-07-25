package com.fennecmomo.momolib;

// MomoLib 可配置常量集中管理
public final class MomoLibConfig
{
    private MomoLibConfig() {}

    // ===================== StateBar =====================

    // 颜色阈值：绿（高于此值）/ 橙（中间）/ 红（低于等于此值）
    public static final float BAR_COLOR_GREEN_THRESHOLD = 0.5f;
    public static final float BAR_COLOR_ORANGE_THRESHOLD = 0.2f;

    // 百分比格式乘数
    public static final float BAR_PERCENT_MULTIPLIER = 100f;
    public static final float BAR_DEFAULT_MAX = 100f;

    // 布局比例：label : value : gap : bar = 2 : 2 : 1 : 4
    public static final int BAR_LABEL_RATIO = 2;
    public static final int BAR_VALUE_RATIO = 2;
    public static final int BAR_GAP_RATIO = 1;
    public static final int BAR_BAR_RATIO = 4;
    public static final int BAR_TOTAL_RATIO = 9;

    // 垂直内边距
    public static final int BAR_VERTICAL_PADDING = 3;

    // ===================== ScrollBar =====================

    public static final int SCROLL_MOUSE_ZONE_WIDTH = 152;
    public static final int SCROLL_TRACK_PADDING = 2;
    public static final int SCROLL_TRACK_TOP_INSET = 1;
    public static final int SCROLL_THUMB_MIN_HEIGHT = 8;

    // ===================== GenericContainerMenu =====================

    public static final int PLAYER_INVENTORY_SIZE = 36;
}
