package com.elilouve.wingsclassic.net;

import com.elilouve.wingsclassic.WingsClassic;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class WingsNetwork {
    private WingsNetwork() {}

    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(WingsClassic.ID, "main"),
        () -> PROTOCOL,
        PROTOCOL::equals,
        PROTOCOL::equals
    );

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, ControlFlyingMessage.class, ControlFlyingMessage::encode, ControlFlyingMessage::decode, ControlFlyingMessage::handle);
        CHANNEL.registerMessage(id++, SyncFlightMessage.class, SyncFlightMessage::encode, SyncFlightMessage::decode, SyncFlightMessage::handle);
    }
}
