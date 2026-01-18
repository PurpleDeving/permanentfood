package net.purple.permfood.moddata;

import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.purple.permfood.Networking.packet.PlayerValuesData;
import net.purple.permfood.config.ConfigAttributes;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;
import static net.purple.permfood.Constants.*;
import static net.purple.permfood.PermanentFood.MODID;
import static net.purple.permfood.moddata.ModData.PLAYER_VALUES;

@EventBusSubscriber(modid = MODID)
public final class PlayerValueEvents {


    //Booth Sides
    @SubscribeEvent(priority = EventPriority.LOW) // Guaranteed to trigger after solcarrot
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        // I dont need to limit here, but it makes no sense to update if nothing happened on solcarrot side
        if (SOLCarrotConfig.limitProgressionToSurvival() && player.isCreative()) return;

        // If its not food, what the fuck am I doing here?
        var usedStack = event.getItem();
        if (usedStack.getFoodProperties(player) == null) return;


        // TODO - Booth Attributes and updateValues should print out new reached Milestones to the player ingame chat + particles and all tha.
        //  But should not happen if Milestones are re-calculated on login/dimension change/config reload

        updatePlayerAttributesAndValues(player);
    }


    // Is Server side online
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {


        Player player = event.getEntity();

        updatePlayerAttributesAndValues(player);
        syncPlayerValues(player);
    }

    private static void syncPlayerValues(Player player) {
        PacketDistributor.sendToPlayer((ServerPlayer) player, new PlayerValuesData(player.getData(PLAYER_VALUES)));
    }

    // Is Server side online
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        updatePlayerAttributesAndValues(player);
        syncPlayerValues(player);
    }

    // Unkown if it fires on client
    @SubscribeEvent
    public static void onPlayerDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        updatePlayerAttributesAndValues(player);
        syncPlayerValues(player);
    }

    // Unkown if it fires on client. Safety check
    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        if ((event.getConfig().getModId().equals(MODID) || event.getConfig().getModId().equals(SOL_CARROT)) && EffectiveSide.get().isServer()) {
            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                updatePlayerAttributesAndValues(player);
                syncPlayerValues(player);
            }
        }
    }

    private static void updatePlayerAttributesAndValues(Player player) {

        // Client should get values through sync and Attribute-Autosync
        if (EffectiveSide.get().isClient()) {
            return;
        }

        int foodCount = FoodList.get(player).getEatenFoodCount();


        // PlayerValues

        player.getData(PLAYER_VALUES).updateValues(foodCount);

        // Attribute modifiers

        if (ConfigAttributes.ENABLE_ARMOR_CHANGES.getAsBoolean()) {
            double amount_armor = PlayerValues.getAttributeValue(ConfigAttributes.ARMOR_PER_MILESTONE.getAsDouble(), ConfigAttributes.MILESTONES_FOR_ARMOR.get(), foodCount);
            AttributeModifier modifier_armor = new AttributeModifier(resourceLocationFoodArmorBuff, amount_armor, ADD_VALUE);
            player.getAttributes().getInstance(Attributes.ARMOR).addOrReplacePermanentModifier(modifier_armor);

            double amount_armor_toughness = PlayerValues.getAttributeValue(ConfigAttributes.ARMOR_TOUGHNESS_PER_MILESTONE.getAsDouble(), ConfigAttributes.MILESTONES_FOR_ARMOR_TOUGHNESS.get(), foodCount);
            AttributeModifier modifier_toughness = new AttributeModifier(resourceLocationFoodArmorToughnessBuff, amount_armor_toughness, ADD_VALUE);
            player.getAttributes().getInstance(Attributes.ARMOR_TOUGHNESS).addOrReplacePermanentModifier(modifier_toughness);
        }

        if (ConfigAttributes.ENABLE_ATTACK_DAMAGE_CHANGES.getAsBoolean()) {
            double amount_attack_damage = PlayerValues.getAttributeValue(ConfigAttributes.ATTACK_DAMAGE_PER_MILESTONE.getAsDouble(), ConfigAttributes.MILESTONES_FOR_ATTACK_DAMAGE.get(), foodCount);
            AttributeModifier modifier_attack_damage = new AttributeModifier(resourceLocationFoodAttackDamageBuff, amount_attack_damage, ADD_VALUE);
            player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).addOrReplacePermanentModifier(modifier_attack_damage);
        }

        if (ConfigAttributes.ENABLE_LUCK_CHANGES.getAsBoolean()) {
            double amount_luck = PlayerValues.getAttributeValue(ConfigAttributes.LUCK_PER_MILESTONE.getAsDouble(), ConfigAttributes.MILESTONES_FOR_LUCK.get(), foodCount);
            AttributeModifier modifier_luck = new AttributeModifier(resourceLocationFoodLuckBuff, amount_luck, ADD_VALUE);
            player.getAttributes().getInstance(Attributes.LUCK).addOrReplacePermanentModifier(modifier_luck);
        }

        if (ConfigAttributes.ENABLE_KNOCKBACK_RESISTANCE_CHANGES.getAsBoolean()) {
            double amount_kb = PlayerValues.getAttributeValue(ConfigAttributes.KNOCKBACK_RESISTANCE_PER_MILESTONE.getAsDouble(), ConfigAttributes.MILESTONES_FOR_KNOCKBACK_RESISTANCE.get(), foodCount);
            AttributeModifier modifier_kb = new AttributeModifier(resourceLocationFoodKnockbackResistanceBuff, amount_kb, ADD_VALUE);
            // Use KNOCKBACK_RESISTANCE attribute (constant name depends on mappings)
            player.getAttributes().getInstance(Attributes.KNOCKBACK_RESISTANCE).addOrReplacePermanentModifier(modifier_kb);
        }


    }
    // Add all the cases a player fucking changes and change the player in its foodData
}
