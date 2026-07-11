package com.fennecmomo.momolib.template;

import com.fennecmomo.momolib.MomoLib;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = MomoLib.MODID, value = Dist.CLIENT)
public class TemplateClientRegistration
{
    @SubscribeEvent
    static void registerScreens(RegisterMenuScreensEvent event)
    {
        event.register(TemplateRegistration.CONFIRM_MENU.get(), ConfirmScreen::new);
    }
}
