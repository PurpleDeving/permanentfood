package net.purple.solextended.api;


import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.purple.solextended.api.milestonebased.MilestoneManagerRegistry;
import net.purple.solextended.config.Configs;
import net.purple.solextended.foodlist.FoodList;

import static net.purple.permfood.PermanentFood.MODID;
import static net.purple.solextended.SolExtended.FOOD_LIST_ATTACHMENT;

@EventBusSubscriber(modid = MODID)
public class SolextendedEvents {


    private static void refreshAllManagers(ServerPlayer player) {
        var foodList = player.getData(FOOD_LIST_ATTACHMENT);
        MilestoneManagerRegistry.updateAllManagers(player, foodList.getFoodEatenCount());
    }

    /**
     * When a player logs in, initialize all milestone managers based on their current food count.
     */
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            refreshAllManagers(serverPlayer);
        }


    }


    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) {
            return;
        }
        if (Configs.solExtendedConfig.resetFoodListOnDeath) {
            serverPlayer.getData(FOOD_LIST_ATTACHMENT).clearList();
            refreshAllManagers(serverPlayer);
        }

    }

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getModId().equals(MODID)) {

            FoodList.updateAllowedFoods(); //on Booth Sides

            if (EffectiveSide.get().isServer()) {
                MilestoneManagerRegistry.updateAllPlayersAllManagers(); // Refresh all players' milestone managers when config is reloaded
            }

        }
    }
 
}
