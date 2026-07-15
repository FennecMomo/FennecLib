package com.fennecmomo.momolib.template.Register;

import com.fennecmomo.momolib.MomoLib;
import com.fennecmomo.momolib.template.Data.ConfirmPopupMenu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// 服务端注册项管理。
// 通过 DeferredRegister 注册所有菜单类型到 NeoForge。
public class TemplateRegistration
{
    // ===================== 注册表 =====================

    // 菜单类型注册表
    static final DeferredRegister<MenuType<?>> MENUS
        = DeferredRegister.create(Registries.MENU, MomoLib.MODID);

    // 确认弹窗菜单类型
    public static final DeferredHolder<MenuType<?>, MenuType<ConfirmPopupMenu>> CONFIRM_MENU
        = MENUS.register(ConfirmPopupMenu.MENU_ID,
                () -> IMenuTypeExtension.create(ConfirmPopupMenu::new));

    // ===================== 注册入口 =====================
    // 将菜单注册表挂载到模组事件总线。
    // modBus: NeoForge 模组事件总线
    public static void register(IEventBus modBus)
    {
        MENUS.register(modBus);
    }
}
