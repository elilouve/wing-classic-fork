package com.elilouve.wingsclassic.flight;

import com.elilouve.wingsclassic.WingsClassic;
import com.elilouve.wingsclassic.net.SyncFlightPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/** Accès à l'état de vol d'un joueur (rattaché au joueur par le système d'« attachments » de NeoForge). */
public final class Flights {
    private Flights() {}

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, WingsClassic.ID);

    /**
     * Pas de copyOnDeath : mourir arrête le vol. Lors d'un clonage sans mort (retour de l'End),
     * NeoForge recopie l'état automatiquement.
     */
    public static final Supplier<AttachmentType<FlightDefault>> FLIGHT = ATTACHMENTS.register(
        "flight",
        () -> AttachmentType.<CompoundTag, FlightDefault>serializable(Flights::create).build()
    );

    private static FlightDefault create(final IAttachmentHolder holder) {
        final FlightDefault flight = new FlightDefault();
        if (holder instanceof ServerPlayer player) {
            flight.registerFlyingListener(isFlying -> {
                if (isFlying) {
                    player.stopRiding();
                }
            });
            final Flight.Notifier notifier = Flight.Notifier.of(
                () -> PacketDistributor.sendToPlayer(player, payload(player, flight)),
                other -> PacketDistributor.sendToPlayer(other, payload(player, flight)),
                () -> PacketDistributor.sendToPlayersTrackingEntity(player, payload(player, flight))
            );
            flight.registerSyncListener(players -> players.notify(notifier));
        }
        return flight;
    }

    private static SyncFlightPayload payload(final Player player, final Flight flight) {
        return new SyncFlightPayload(player.getId(), flight.isFlying(), flight.getTimeFlying());
    }

    @Nullable
    public static Flight get(final Entity entity) {
        if (entity instanceof Player) {
            return entity.getData(FLIGHT.get());
        }
        return null;
    }

    public static void ifPlayer(final Entity entity, final BiConsumer<Player, Flight> action) {
        final Flight flight = get(entity);
        if (flight != null) {
            action.accept((Player) entity, flight);
        }
    }
}
