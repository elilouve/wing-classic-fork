package com.elilouve.wingsclassic.client.view;

import com.elilouve.wingsclassic.flight.Flight;
import com.elilouve.wingsclassic.flight.Flights;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.WeakHashMap;

/** Retrouve (ou crée) la vue d'ailes d'une entité. Côté client uniquement. */
public final class FlightViews {
    private FlightViews() {}

    private static final Map<LivingEntity, FlightView> VIEWS = new WeakHashMap<>();

    public static FlightView get(final LivingEntity entity) {
        return VIEWS.computeIfAbsent(entity, e -> {
            if (e instanceof Player player) {
                final Flight flight = Flights.get(player);
                if (flight != null) {
                    return new FlightViewDefault(player, flight);
                }
            }
            return new FlightViewStatic(e);
        });
    }
}
