package com.elilouve.wingsclassic.client;

import com.elilouve.wingsclassic.flight.Flights;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

/** Code exécuté uniquement côté client à la réception d'un paquet. */
public final class ClientNet {
    private ClientNet() {}

    public static void applySync(final int playerId, final boolean isFlying, final int timeFlying) {
        final Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        final Entity entity = mc.level.getEntity(playerId);
        if (entity != null) {
            Flights.ifPlayer(entity, (player, flight) -> {
                flight.setIsFlying(isFlying);
                flight.setTimeFlying(timeFlying);
            });
        }
    }
}
