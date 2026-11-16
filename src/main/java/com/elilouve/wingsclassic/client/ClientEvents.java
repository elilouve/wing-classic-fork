package com.elilouve.wingsclassic.client;

import com.elilouve.wingsclassic.WingsClassic;
import com.elilouve.wingsclassic.flight.Flight;
import com.elilouve.wingsclassic.flight.Flights;
import com.elilouve.wingsclassic.net.ControlFlyingPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = WingsClassic.ID, value = Dist.CLIENT)
public final class ClientEvents {
    private ClientEvents() {}

    @SubscribeEvent
    public static void onEntityJoinLevel(final EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide && event.getEntity() instanceof LocalPlayer player) {
            Flights.ifPlayer(player, (p, flight) -> {
                if (flight.claimClientHook()) {
                    // Quand le joueur local change d'état, on prévient le serveur.
                    final Flight.Notifier notifier = Flight.Notifier.of(
                        () -> {},
                        other -> {},
                        () -> PacketDistributor.sendToServer(new ControlFlyingPayload(flight.isFlying()))
                    );
                    flight.registerSyncListener(players -> players.notify(notifier));
                    Minecraft.getInstance().getSoundManager().play(new WingsFlySound(player, flight));
                }
            });
        }
    }

    @SubscribeEvent
    public static void onClientTick(final ClientTickEvent.Post event) {
        final Minecraft mc = Minecraft.getInstance();
        final LocalPlayer player = mc.player;
        while (WingsKeys.FLY.consumeClick()) {
            if (player == null) {
                continue;
            }
            final Flight flight = Flights.get(player);
            if (flight != null && (flight.isFlying() || flight.canFly(player))) {
                flight.toggleIsFlying(Flight.PlayerSet.ofOthers());
            }
        }
    }
}
