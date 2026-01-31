package net.purple.solextended.networking;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import static net.purple.solextended.SolExtended.FOOD_LIST_ATTACHMENT;

public class ServerPayLoadHandler {

    // HERE WE ARE ON THE CLIENT
    public static void handleFoodListOnClient(FoodListData foodListData, IPayloadContext context) {
        context.player().setData(FOOD_LIST_ATTACHMENT, foodListData.eatenfoods());
    }
}
