package net.purple.solextended.foodlist;


import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

@EventBusSubscriber
public class FoodListEvents {

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

        //IMPL Update PlayerFoodList for that Player
        //IMPL Sync PlayerFoodList to Client
        //IMPL Update all Dependent Systems

        // TODO Check and Ask why SolCarrot is using the invalidateProgressCache
    }


}
