package net.purple.permfood.Networking;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.purple.permfood.Networking.packet.PlayerValuesData;

import static net.purple.permfood.PermanentFood.MODID;
import static net.purple.permfood.moddata.ModData.PLAYER_VALUES;

@EventBusSubscriber(modid = MODID)
public class ServerPayLoadHandler {

    // This Method is on Client
    public static void handlePlayerValueOnClient(PlayerValuesData playerValuesData, IPayloadContext context) {
        context.player().setData(PLAYER_VALUES, playerValuesData.newValues());
    }


    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.MAIN);

        // Use the PlayerValuesData codec so the registrar decodes the full packet object
        registrar.playToClient(PlayerValuesData.TYPE, PlayerValuesData.STREAM_CODEC, ServerPayLoadHandler::handlePlayerValueOnClient);
    }
}
