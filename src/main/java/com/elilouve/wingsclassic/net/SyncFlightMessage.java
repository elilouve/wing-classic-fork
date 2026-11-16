package com.elilouve.wingsclassic.net;

import com.elilouve.wingsclassic.client.ClientNet;
import com.elilouve.wingsclassic.flight.Flight;
import com.elilouve.wingsclassic.flight.FlightDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Serveur -> client : état de vol d'un joueur. */
public final class SyncFlightMessage {
    private final int playerId;
    private final Flight flight;

    public SyncFlightMessage(final int playerId, final Flight flight) {
        this.playerId = playerId;
        this.flight = flight;
    }

    public static void encode(final SyncFlightMessage msg, final FriendlyByteBuf buf) {
        buf.writeVarInt(msg.playerId);
        msg.flight.serialize(buf);
    }

    public static SyncFlightMessage decode(final FriendlyByteBuf buf) {
        final int id = buf.readVarInt();
        final FlightDefault flight = new FlightDefault();
        flight.deserialize(buf);
        return new SyncFlightMessage(id, flight);
    }

    public static void handle(final SyncFlightMessage msg, final Supplier<NetworkEvent.Context> ctx) {
        final NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientNet.applySync(msg.playerId, msg.flight)));
        context.setPacketHandled(true);
    }
}
