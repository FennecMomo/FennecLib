package com.fennecmomo.momolib.template;

import com.fennecmomo.momolib.MomoLib;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TemplateRegistration
{
    static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, MomoLib.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<ConfirmMenu>> CONFIRM_MENU =
            MENUS.register("confirm_menu",
                    () -> IMenuTypeExtension.create(ConfirmMenu::new));

    public static void register(net.neoforged.bus.api.IEventBus modBus)
    {
        MENUS.register(modBus);
    }
}
