package com.elilouve.wingsclassic.net;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class WingsNetwork {
    private WingsNetwork() {}

    private static final String PROTOCOL = "1";

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PROTOCOL);
        registrar.playToServer(ControlFlyingPayload.TYPE, ControlFlyingPayload.CODEC, ControlFlyingPayload::handle);
        registrar.playToClient(SyncFlightPayload.TYPE, SyncFlightPayload.CODEC, SyncFlightPayload::handle);
    }
}
