package com.elilouve.wingsclassic.client;

import com.elilouve.wingsclassic.WingsClassic;
import com.elilouve.wingsclassic.registry.WingsItems;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid = WingsClassic.ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ClientModEvents {
    private ClientModEvents() {}

    @SubscribeEvent
    public static void onRegisterKeys(final RegisterKeyMappingsEvent event) {
        event.register(WingsKeys.FLY);
    }

    @SubscribeEvent
    public static void onRegisterItemColors(final RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> tintIndex == 0 ? 0xFF9B172D : 0xFFFFFFFF, WingsItems.BAT_BLOOD.get());
    }
}
