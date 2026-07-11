package com.fennecmomo.momolib.template;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

/**
 * 通用确认/双按钮弹窗 Menu。
 * 使用 {@link ConfirmScreen} 渲染。
 */
public class ConfirmMenu extends AbstractContainerMenu
{
    public final String title;
    public final String info;
    public final String leftLabel;
    public final String leftCommand;
    public final String rightLabel;
    public final String rightCommand;

    public ConfirmMenu(int containerId, Inventory inv,
                       net.minecraft.network.RegistryFriendlyByteBuf buf)
    {
        this(containerId, inv, buf.readUtf(), buf.readUtf(),
                buf.readUtf(), buf.readUtf(), buf.readUtf(), buf.readUtf());
    }

    public ConfirmMenu(int containerId, Inventory inv,
                       String title, String info,
                       String leftLabel, String leftCommand,
                       String rightLabel, String rightCommand)
    {
        super(TemplateRegistration.CONFIRM_MENU.get(), containerId);
        this.title = title;
        this.info = info;
        this.leftLabel = leftLabel;
        this.leftCommand = leftCommand;
        this.rightLabel = rightLabel;
        this.rightCommand = rightCommand;
    }

    @Override public ItemStack quickMoveStack(Player p, int i) { return ItemStack.EMPTY; }
    @Override public boolean stillValid(Player p) { return true; }
}
