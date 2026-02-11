package net.purple.solextended.foodlist;


import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.purple.solextended.config.Configs;
import net.purple.solextended.milestonebased.MilestoneManagerRegistry;
import net.purple.solextended.milestonebased.MilestoneMessage;
import net.purple.solextended.networking.MilestoneCelebrationPayload;

import static net.purple.solextended.Constants.ENABLE_EXTENSIVE_LOGGING;
import static net.purple.solextended.SolExtended.*;

@EventBusSubscriber(modid = MODID)
public class FoodListEatEvent {

    /******************************************
     Sync PlayerFoodList to client. FoodList can be created on client because Configs are synced.
     ******************************************/


    //Booth Sides
    @SubscribeEvent(priority = EventPriority.HIGH)
    // High should be enough to Trigger before all mods that use this as dependency.
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

        if (!newFoodEaten) {
            return;
        }

        if (IS_DEV && ENABLE_EXTENSIVE_LOGGING) {
            LOGGER.info("FoodListEvents.onFoodEaten: New food eaten registered: {}", usedStack.getDisplayName());
        }

        MilestoneManagerRegistry.syncFoodListAndUpdateAllManagersForPlayer(player, playerFoodList.getFoodEatenCount());

        // Everything below is server-authored.
        if (EffectiveSide.get().isClient()) {
            return;
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            if (IS_DEV) {
                LOGGER.error("FoodListEvents.onFoodEaten: The useless check is reached and successfully, why?");
            }
            return;
        }

        MilestoneMessage milestoneMessage = MilestoneManagerRegistry.collectMilestoneMessagesForPlayer(serverPlayer);

        for (MutableComponent message : milestoneMessage.milestoneMessages) {
            // Send chat message from the server side.
            serverPlayer.sendSystemMessage(message);
        }

        // Send simplified celebration payload to clients.
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                serverPlayer,
                new MilestoneCelebrationPayload(
                        serverPlayer.getId(),
                        milestoneMessage.anyMilestoneReached,
                        milestoneMessage.anyMaxMilestoneReached
                )
        );


    }
}
