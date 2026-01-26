package net.purple.permfood.moddata;

import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.purple.permfood.config.Configs;
import net.purple.permfood.config.FoodSystemConfig;
import net.purple.permfood.moddata.attributes.ModAttributes;
import net.purple.permfood.moddata.attributes.PlayerAttributes;

import static net.purple.permfood.Constants.SOL_CARROT;
import static net.purple.permfood.PermanentFood.MODID;
import static net.purple.permfood.moddata.attributes.ModAttributes.MAX_HUNGER;
import static net.purple.permfood.moddata.attributes.PlayerAttributes.PLAYER_ATTRIBUTES;

@EventBusSubscriber(modid = MODID)
public class PlayerAttributeEvents {

    @SubscribeEvent // From ModAttributes overwrite the defaults when needed.
    public static void modifyDefaultAttributes(EntityAttributeModificationEvent event) {

        FoodSystemConfig config = Configs.foodSystemConfig;

        if (config.sectionHunger.ENABLE_HUNGER_CHANGES) {
            event.add(EntityType.PLAYER, MAX_HUNGER, config.sectionHunger.baseHunger.get());
        }

        if (config.sectionSaturation.ENABLE_SATURATION_CHANGES) {
            event.add(EntityType.PLAYER, ModAttributes.MAX_SATURATION, config.sectionSaturation.baseSaturation.get());
        }

        if (config.sectionExhaustion.ENABLE_EXHAUSTION_CHANGES) {
            event.add(EntityType.PLAYER, ModAttributes.MAX_EXHAUSTION, config.sectionExhaustion.baseExhaustion.get());
        }

    }

    // Server only and only on actual Respawn
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {

    }

    // Is Server side only
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        updatePlayerAttributes(player);
    }

    // Is Server side only
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        updatePlayerAttributes(player);
    }

    @SubscribeEvent
    public static void onPlayerDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        updatePlayerAttributes(player);
    }

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {

        if ((event.getConfig().getModId().equals(MODID) || event.getConfig().getModId().equals(SOL_CARROT)) && EffectiveSide.get().isServer()) {

            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                updatePlayerAttributes(player);
            }
        }

        // client side intentionally does nothing
    }


    protected static void updatePlayerAttributes(Player player) { //TODO - Isnt this double with the other one? with this getOrCreatePlayerAttributes

        int foodCount = FoodList.get(player).getProgressInfo().foodsEaten;

        if (!PLAYER_ATTRIBUTES.containsKey(player.getUUID())) {
            PLAYER_ATTRIBUTES.put(player.getUUID(), new PlayerAttributes(player, foodCount));
        }

        PlayerAttributes.getPlayerAttributes(player).updateBuffs(foodCount);
    }

}
