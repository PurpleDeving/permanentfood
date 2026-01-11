package net.purple.permfood.moddata;

import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.purple.permfood.Constants;
import net.purple.permfood.Networking.packet.PlayerValuesData;
import net.purple.permfood.config.Config;
import net.purple.permfood.config.ConfigAttributes;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;
import static net.purple.permfood.Constants.*;
import static net.purple.permfood.PermanentFood.MODID;
import static net.purple.permfood.moddata.ModData.PLAYER_VALUES;

@EventBusSubscriber(modid = MODID)
public final class PlayerValueEvents {


    // Is Server side online
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        Player player = event.getEntity();
        updatePlayerValues(player);
        syncPlayerValues(player);


    }

    private static void syncPlayerValues(Player player) {
        PacketDistributor.sendToPlayer((ServerPlayer) player, new PlayerValuesData(player.getData(PLAYER_VALUES)));
    }

    private static void updatePlayerValues(Player player) {

        // Client should get values through sync and Attribute-Autosync
        if (EffectiveSide.get().isClient()) {
            return;
        }

        int foodCount = FoodList.get(player).getEatenFoodCount();
        player.getData(PLAYER_VALUES).updateValues(foodCount);

        if (ConfigAttributes.ENABLE_ARMOR_CHANGES.getAsBoolean()) {
            double amount_armor = PlayerValues.getValueDouble(Constants.VANILLA_BASE_ARMOR, ConfigAttributes.ARMOR_PER_MILESTONE.getAsDouble(), ConfigAttributes.MILESTONES_FOR_ARMOR.get(), foodCount);
            AttributeModifier modifier_armor = new AttributeModifier(resourceLocationFoodArmorBuff, amount_armor, ADD_VALUE);
            player.getAttributes().getInstance(Attributes.ARMOR).addOrReplacePermanentModifier(modifier_armor);

            double amount_armor_toughness = PlayerValues.getValueDouble(Constants.VANILLA_BASE_ARMOR_TOUGHNESS, ConfigAttributes.ARMOR_TOUGHNESS_PER_MILESTONE.getAsDouble(), ConfigAttributes.MILESTONES_FOR_ARMOR_TOUGHNESS.get(), foodCount);
            AttributeModifier modifier_toughness = new AttributeModifier(resourceLocationFoodArmorToughnessBuff, amount_armor_toughness, ADD_VALUE);
            player.getAttributes().getInstance(Attributes.ARMOR).addOrReplacePermanentModifier(modifier_toughness);
        }


    }


    // Is Server side online
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        updatePlayerValues(player);
        syncPlayerValues(player);
    }

    // Unkown if it fires on client
    @SubscribeEvent
    public static void onPlayerDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        updatePlayerValues(player);
        syncPlayerValues(player);
    }

    // Unkown if it fires on client. Safety check
    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        if ((event.getConfig().getModId().equals(MODID) || event.getConfig().getModId().equals(SOL_CARROT)) && EffectiveSide.get().isServer()) {
            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                updatePlayerValues(player);
                syncPlayerValues(player);
            }
        }
    }


    // Add all the cases a player fucking changes and change the player in its foodData
}
