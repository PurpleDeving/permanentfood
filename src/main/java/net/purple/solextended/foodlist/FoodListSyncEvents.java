package net.purple.solextended.foodlist;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.purple.solextended.networking.FoodListData;

import static net.purple.solextended.SolExtended.FOOD_LIST_ATTACHMENT;
import static net.purple.solextended.SolExtended.MODID;

@EventBusSubscriber(modid = MODID)
public class FoodListSyncEvents {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        // Server needs to send any loaded data to the client
        syncFoodList(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncFoodList(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncFoodList(event.getEntity());
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        // Copy food list data to the new player instance
        var originalPlayer = event.getOriginal();
        var original = originalPlayer.getData(FOOD_LIST_ATTACHMENT);
        event.getEntity().setData(FOOD_LIST_ATTACHMENT, original);
    }

    /**
     * Syncs the player's food list to their client.
     */
    public static void syncFoodList(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            PlayerFoodList foodList = player.getData(FOOD_LIST_ATTACHMENT);
            PacketDistributor.sendToPlayer(
                    serverPlayer,
                    new FoodListData(foodList.serializeNBT(player.registryAccess()))
            );
        }
    }
}
