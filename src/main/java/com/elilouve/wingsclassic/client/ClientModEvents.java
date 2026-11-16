package com.elilouve.wingsclassic.client;

import com.elilouve.wingsclassic.WingsClassic;
import com.elilouve.wingsclassic.registry.WingsItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WingsClassic.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ClientModEvents {
    private ClientModEvents() {}

    @SubscribeEvent
    public static void onRegisterKeys(final RegisterKeyMappingsEvent event) {
        event.register(WingsKeys.FLY);
    }

    @SubscribeEvent
    public static void onRegisterItemColors(final RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> tintIndex == 0 ? 0x9B172D : 0xFFFFFF, WingsItems.BAT_BLOOD.get());
    }
}
