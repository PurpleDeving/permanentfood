package net.purple.solextended.foodlist;


import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.purple.solextended.config.Configs;

import static net.purple.solextended.SolExtended.FOOD_LIST_ATTACHMENT;

@EventBusSubscriber
public class FoodListEventProducer {

    /******************************************
     Sync PlayerFoodList to client. FoodList can be created on client because Configs are synced.
     ******************************************/


    //Booth Sides
    @SubscribeEvent(priority = EventPriority.HIGH)
    // High should be enough to Trigger before all mopds that use this as dependency.
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {

        if (EffectiveSide.get().isClient()) {
            return;
        }
        ServerPlayer player = (ServerPlayer) event.getEntity();

        if (Configs.solExtendedConfig.limitProgressToSurvival && player.isCreative()) return;

        var usedStack = event.getItem();
        if (usedStack.getFoodProperties(player) == null) return;


        player.getData(FOOD_LIST_ATTACHMENT).addFood(usedStack);


        // TODO: retrieve the PlayerFoodList attachment/capability from the player here.
        // TODO: update PlayerFoodList for that player, then sync to client.
        // (left intentionally unimplemented)

        //IMPL Update PlayerFoodList to Client
        //IMPL Update all Dependent Systems

        // TODO Check and Ask why SolCarrot is using the invalidateProgressCache
    }


}
