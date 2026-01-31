package net.purple.solextended.api.milestonebased;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.purple.solextended.SolExtended;

import static net.purple.permfood.PermanentFood.MODID;

/**
 * Handles player lifecycle events for milestone managers.
 * Ensures milestone managers are properly initialized and synced when players join, respawn, etc.
 */
@EventBusSubscriber(modid = MODID)
public class PlayerMilestoneEvents {

    private static void refreshAllManagers(ServerPlayer player) {
        var foodList = player.getData(SolExtended.FOOD_LIST_ATTACHMENT);
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

    /**
     * When a player changes dimension, ensure all milestone managers are maintained.
     */
    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            refreshAllManagers(serverPlayer);
        }
    }

    /**
     * When a player respawns (death or end return), maintain milestone manager data.
     * The DataAttachment system automatically handles cloning if needed.
     */
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            refreshAllManagers(serverPlayer);
        }
    }

    /**
     * Handle player cloning (death, end portal return).
     * DataAttachment automatically handles this, but we ensure all managers are re-applied.
     */
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer && !event.isWasDeath()) {
            refreshAllManagers(serverPlayer);
        }
    }
}
