package com.fennecmomo.momolib;

import com.fennecmomo.momolib.template.TemplateRegistration;
import net.neoforged.fml.common.Mod;

@Mod(MomoLib.MODID)
public class MomoLib
{
    public static final String MODID = "momolib";

    public MomoLib(net.neoforged.bus.api.IEventBus modBus)
    {
        TemplateRegistration.register(modBus);
    }
}
