package net.purple.solextended.foodlist;


import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.purple.solextended.api.milestonebased.MilestoneManagerRegistry;
import net.purple.solextended.config.Configs;

import java.util.ArrayList;
import java.util.List;

import static net.purple.solextended.Constants.ENABLE_EXTENSIVE_LOGGING;
import static net.purple.solextended.SolExtended.*;

@EventBusSubscriber(modid = MODID)
public class FoodListEatEvent {

    /******************************************
     Sync PlayerFoodList to client. FoodList can be created on client because Configs are synced.
     ******************************************/


    //Booth Sides
    @SubscribeEvent(priority = EventPriority.HIGH)
    // High should be enough to Trigger before all mopds that use this as dependency.
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {

        if (IS_DEV && ENABLE_EXTENSIVE_LOGGING) {
            LOGGER.info("FoodListEvents.onFoodEaten triggered");
        }

        Player player = (Player) event.getEntity();

        if (Configs.solExtendedConfig.limitProgressToSurvival && player.isCreative()) return;


        var usedStack = event.getItem();
        if (usedStack.getFoodProperties(player) == null) return;

        player.syncData(FOOD_LIST_ATTACHMENT);

        PlayerFoodList playerFoodList = player.getData(FOOD_LIST_ATTACHMENT);
        boolean newFoodEaten = playerFoodList.addFood(usedStack);

        if (newFoodEaten) {

            if (IS_DEV && ENABLE_EXTENSIVE_LOGGING) {
                LOGGER.info("FoodListEvents.onFoodEaten: New food eaten registered: {}", usedStack.getDisplayName());
            }
            //For now immediate sync. Maybe needs changing when celebration becomes a thing
            MilestoneManagerRegistry.syncFoodListAndUpdateAllManagersForPlayer(player, playerFoodList.getFoodEatenCount());
        } else {
            return;
        }

        List<?> celebrations = new ArrayList<>(); // TODO get from MilestoneManagerRegistry > From all Managers > from all there MilestoneProgressions.
        boolean milestonesReached = false;
        if (milestonesReached && EffectiveSide.get().isClient()) {
            // TODO Celebrate
        }
    }
}
