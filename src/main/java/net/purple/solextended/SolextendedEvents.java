package net.purple.solextended;


import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.purple.solextended.config.Configs;
import net.purple.solextended.foodlist.FoodList;
import net.purple.solextended.milestonebased.MilestoneManagerRegistry;
import net.purple.solextended.networking.SolExtendedNetworking;

import static net.purple.solextended.SolExtended.FOOD_LIST_ATTACHMENT;
import static net.purple.solextended.SolExtended.MODID;

@EventBusSubscriber(modid = MODID)
public class SolextendedEvents {

    /**
     * When a player logs in, initialize all milestone managers based on their current food count.
     */
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            MilestoneManagerRegistry.syncFoodListAndUpdateAllManagersForPlayer(serverPlayer);
        }


    }


    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) {
            return;
        }
        if (Configs.solExtendedConfig.resetFoodListOnDeath) {
            serverPlayer.getData(FOOD_LIST_ATTACHMENT).clearList();
            MilestoneManagerRegistry.syncFoodListAndUpdateAllManagersForPlayer(serverPlayer);
        }

    }


    /*
       Info: Because all MilestoneTypes need to be static, a reloading here doesn't make sense. Instead, all Configs relevant for MilestoneTypes should have a @RequiresAction(action = Action.RESTART)
     */

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getModId().equals(MODID)) {

            FoodList.updateAllowedFoods(); //on Booth Sides

            if (EffectiveSide.get().isServer()) {
                MilestoneManagerRegistry.updateAllManagersForAllPlayers(); // Refresh all players' milestone managers when config is reloaded
            }

        }
    }

    /**
     * Register custom payloads.
     */
    @SubscribeEvent
    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        SolExtendedNetworking.registerPayloads(event);
    }

}
