package net.purple.solextended.networking;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.purple.solextended.foodlist.PlayerFoodList;

import static net.purple.solextended.SolExtended.FOOD_LIST_ATTACHMENT;
import static net.purple.solextended.SolExtended.MODID;

@EventBusSubscriber(modid = MODID)
public class ServerPayLoadHandler {

    // HERE WE ARE ON THE CLIENT
    public static void handleFoodListOnClient(FoodListData foodListData, IPayloadContext context) {
        Player player = context.player();
        PlayerFoodList playerFoodList = player.getData(FOOD_LIST_ATTACHMENT);
        playerFoodList.deserializeNBT(player.registryAccess(), foodListData.foodListNBT());
    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.MAIN);

        registrar.playToClient(FoodListData.TYPE, FoodListData.CODEC, ServerPayLoadHandler::handleFoodListOnClient);
    }
}
