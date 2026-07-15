package com.fennecmomo.momolib.template;

import com.fennecmomo.momolib.MomoLib;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

// 客户端菜单屏幕注册。
// 监听 RegisterMenuScreensEvent，将服务端菜单类型绑定到对应的客户端 Screen。
@EventBusSubscriber(modid = MomoLib.MODID, value = Dist.CLIENT)
public class TemplateClientRegistration {
    // ===================== 事件处理 =====================

    // 将 ConfirmMenu 菜单类型绑定到 ConfirmScreen 渲染。
    @SubscribeEvent
    static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(TemplateRegistration.CONFIRM_MENU.get(), ConfirmScreen::new);
    }
}
