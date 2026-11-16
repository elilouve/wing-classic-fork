package com.elilouve.wingsclassic.client;

import com.elilouve.wingsclassic.flight.Flight;
import com.elilouve.wingsclassic.flight.Flights;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

/** Code exécuté uniquement côté client à la réception d'un paquet. */
public final class ClientNet {
    private ClientNet() {}

    public static void applySync(final int playerId, final Flight received) {
        final Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        final Entity entity = mc.level.getEntity(playerId);
        if (entity != null) {
            Flights.ifPlayer(entity, (player, flight) -> flight.clone(received));
        }
    }
}
