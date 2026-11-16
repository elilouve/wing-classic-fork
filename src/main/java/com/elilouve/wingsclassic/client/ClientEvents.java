package com.elilouve.wingsclassic.client;

import com.elilouve.wingsclassic.WingsClassic;
import com.elilouve.wingsclassic.flight.Flight;
import com.elilouve.wingsclassic.flight.Flights;
import com.elilouve.wingsclassic.net.ControlFlyingMessage;
import com.elilouve.wingsclassic.net.WingsNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WingsClassic.ID, value = Dist.CLIENT)
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
                        () -> WingsNetwork.CHANNEL.sendToServer(new ControlFlyingMessage(flight.isFlying()))
                    );
                    flight.registerSyncListener(players -> players.notify(notifier));
                    Minecraft.getInstance().getSoundManager().play(new WingsFlySound(player, flight));
                }
            });
        }
    }

    @SubscribeEvent
    public static void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
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
