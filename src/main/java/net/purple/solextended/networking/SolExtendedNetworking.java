package net.purple.solextended.networking;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.purple.solextended.SolExtended;

/**
 * Networking registration for solextended.
 * <p>
 * Registered via {@link net.purple.solextended.SolextendedEvents}.
 */
public final class SolExtendedNetworking {

    private SolExtendedNetworking() {}

    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(SolExtended.MODID).versioned("1");

        registrar.playToClient(
                MilestoneCelebrationPayload.TYPE,
                MilestoneCelebrationPayload.CODEC,
                MilestoneCelebrationClientHandler::handle
        );
    }

}
