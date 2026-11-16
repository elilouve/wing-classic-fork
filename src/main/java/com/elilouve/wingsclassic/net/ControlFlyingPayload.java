package com.elilouve.wingsclassic.net;

import com.elilouve.wingsclassic.WingsClassic;
import com.elilouve.wingsclassic.flight.Flight;
import com.elilouve.wingsclassic.flight.Flights;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Client -> serveur : le joueur active/désactive le vol. */
public record ControlFlyingPayload(boolean isFlying) implements CustomPacketPayload {
    public static final Type<ControlFlyingPayload> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(WingsClassic.ID, "control_flying"));

    public static final StreamCodec<ByteBuf, ControlFlyingPayload> CODEC =
        StreamCodec.composite(ByteBufCodecs.BOOL, ControlFlyingPayload::isFlying, ControlFlyingPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ControlFlyingPayload msg, final IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }
        final Flight flight = Flights.get(player);
        if (flight != null && (!msg.isFlying || flight.canFly(player))) {
            flight.setIsFlying(msg.isFlying, Flight.PlayerSet.ofOthers());
        }
    }
}
