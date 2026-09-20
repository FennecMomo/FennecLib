package com.fennecmomo.fenneclib.template.Register;

import com.fennecmomo.fenneclib.FennecLib;
import com.fennecmomo.fenneclib.template.UI.ConfirmPopupScreen;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

// 客户端菜单屏幕注册。
// 监听 RegisterMenuScreensEvent，将服务端菜单类型绑定到对应的客户端 Screen。
@EventBusSubscriber(modid = FennecLib.MODID, value = Dist.CLIENT)
public class TemplateClientRegistration
{
    // ===================== 事件处理 =====================

    // 将 ConfirmPopupMenu 菜单类型绑定到 ConfirmPopupScreen 渲染。
    @SubscribeEvent
    static void registerScreens(RegisterMenuScreensEvent event)
    {
        event.register(TemplateRegistration.CONFIRM_MENU.get(), ConfirmPopupScreen::new);
    }
}
