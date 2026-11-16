package com.elilouve.wingsclassic.net;

import com.elilouve.wingsclassic.WingsClassic;
import com.elilouve.wingsclassic.client.ClientNet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Serveur -> client : état de vol d'un joueur. */
public record SyncFlightPayload(int playerId, boolean isFlying, int timeFlying) implements CustomPacketPayload {
    public static final Type<SyncFlightPayload> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(WingsClassic.ID, "sync_flight"));

    public static final StreamCodec<ByteBuf, SyncFlightPayload> CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, SyncFlightPayload::playerId,
        ByteBufCodecs.BOOL, SyncFlightPayload::isFlying,
        ByteBufCodecs.VAR_INT, SyncFlightPayload::timeFlying,
        SyncFlightPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final SyncFlightPayload msg, final IPayloadContext context) {
        ClientNet.applySync(msg.playerId, msg.isFlying, msg.timeFlying);
    }
}
