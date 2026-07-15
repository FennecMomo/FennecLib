package com.fennecmomo.momolib;

import com.fennecmomo.momolib.template.TemplateRegistration;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

// MomoLib 模组主入口。
// 构造时通过 TemplateRegistration 注册所有菜单类型到 NeoForge。
@Mod(MomoLib.MODID)
public class MomoLib {
    // ===================== 常量 =====================

    // 模组唯一标识符，用于注册表命名空间、资源路径等
    public static final String MODID = "FM_momolib";

    // ===================== 构造器 =====================
    // 模组初始化入口，注册所有服务端内容（菜单、方块、物品等）。
    // modBus: NeoForge 模组事件总线
    public MomoLib(IEventBus modBus) {
        TemplateRegistration.register(modBus);
    }
}
