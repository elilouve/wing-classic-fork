package com.elilouve.wingsclassic.net;

import com.elilouve.wingsclassic.flight.Flight;
import com.elilouve.wingsclassic.flight.Flights;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Client -> serveur : le joueur active/désactive le vol. */
public final class ControlFlyingMessage {
    private final boolean isFlying;

    public ControlFlyingMessage(final boolean isFlying) {
        this.isFlying = isFlying;
    }

    public static void encode(final ControlFlyingMessage msg, final FriendlyByteBuf buf) {
        buf.writeBoolean(msg.isFlying);
    }

    public static ControlFlyingMessage decode(final FriendlyByteBuf buf) {
        return new ControlFlyingMessage(buf.readBoolean());
    }

    public static void handle(final ControlFlyingMessage msg, final Supplier<NetworkEvent.Context> ctx) {
        final NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            final ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }
            final Flight flight = Flights.get(player);
            if (flight != null && (!msg.isFlying || flight.canFly(player))) {
                flight.setIsFlying(msg.isFlying, Flight.PlayerSet.ofOthers());
            }
        });
        context.setPacketHandled(true);
    }
}
