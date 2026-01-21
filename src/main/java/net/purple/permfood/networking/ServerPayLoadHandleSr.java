package net.purple.permfood.networking;

/*@EventBusSubscriber(modid = MODID) TODO- Evalute if needed
public class ServerPayLoadHandler {

    // This Method is on Client
    public static void handlePlayerValueOnClient(PlayerValuesData playerValuesData, IPayloadContext context) {
        context.player().setData(PLAYER_ATTRIBUTES, playerValuesData.newValues());
    }


    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.MAIN);

        // Use the PlayerValuesData codec so the registrar decodes the full packet object
        //registrar.playToClient(PlayerValuesData.TYPE, PlayerValuesData.STREAM_CODEC, ServerPayLoadHandler::handlePlayerValueOnClient);
    }
}*/
