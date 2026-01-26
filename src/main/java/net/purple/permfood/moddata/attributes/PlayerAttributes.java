package net.purple.permfood.moddata.attributes;

import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.purple.permfood.PermanentFood;
import net.purple.permfood.config.Configs;
import net.purple.permfood.config.FoodSystemConfig;
import net.purple.permfood.moddata.baseClases.PlayerFoodInstance;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;
import static net.purple.permfood.Constants.*;

public class PlayerAttributes extends PlayerFoodInstance {


    public static final HashMap<UUID, PlayerAttributes> PLAYER_ATTRIBUTES = new HashMap<>();

    public static PlayerAttributes getPlayerAttributes(Player player) {
        return PLAYER_ATTRIBUTES.get(player.getUUID());
    }

    // Player Attributes
    private final PlayerAttribute max_hunger;
    private final PlayerAttribute max_saturation;
    private final PlayerAttribute max_exhaustion;

    public PlayerAttributes(Player player, int foodCount) {
        super(player);

        if (player == null) {
            PermanentFood.LOG.warn("Something went wrong while creating PlayerAttributes: Player is null");
        }

        this.max_hunger = new PlayerAttribute(ModAttributes.MAX_HUNGER);
        this.addMilestoneBased(max_hunger);

        this.max_saturation = new PlayerAttribute(ModAttributes.MAX_SATURATION);
        this.addMilestoneBased(max_saturation);

        this.max_exhaustion = new PlayerAttribute(ModAttributes.MAX_EXHAUSTION);
        this.addMilestoneBased(max_exhaustion);

        updateMilestones();
        updatePerMilestone();
        updateBuffs(foodCount);
    }

    @Override
    protected void updateMilestones() {
        FoodSystemConfig config = Configs.foodSystemConfig;
        this.max_hunger.updateMilestones(config.sectionHunger.milestonesHunger.get());
        this.max_saturation.updateMilestones(config.sectionSaturation.milestonesSaturation.get());
        this.max_exhaustion.updateMilestones(config.sectionExhaustion.milestonesExhaustion.get());
    }

    // Not in MilestoneBased in Case there are MilestoneBased with other logic
    private void updatePerMilestone() {
        FoodSystemConfig config = Configs.foodSystemConfig;
        this.max_hunger.setValuePerMilestone(config.sectionHunger.perMilestoneHunger.get());
        this.max_saturation.setValuePerMilestone(config.sectionSaturation.perMilestoneSaturation.get());
        this.max_exhaustion.setValuePerMilestone(config.sectionExhaustion.perMilestoneExhaustion.get());
        // TODO > Add existing attributes here
    }


    @Override
    @SuppressWarnings("unchecked cast")
    public List<PlayerAttribute> getMilestoneBasedList() {
        return (List<PlayerAttribute>) super.getRawMilestoneBasedList();
    }

    @Override
    public void updateBuffs(int foodCount) {

        // ensure milestone counts are always up-to-date before computing added values
        for (PlayerAttribute attribute : getMilestoneBasedList()) {
            attribute.updateMilestonesReached(foodCount);
        }

        AttributeMap attributeMap = super.getPlayer().getAttributes();
        FoodSystemConfig foodConfig = Configs.foodSystemConfig;

        if (foodConfig.sectionHunger.ENABLE_HUNGER_CHANGES) {
            AttributeModifier modifier = new AttributeModifier(rLMaxHungerBuff, this.max_hunger.getAddedValue(), ADD_VALUE);
            attributeMap.getInstance(ModAttributes.MAX_HUNGER).addOrReplacePermanentModifier(modifier);
        } else {
            attributeMap.getInstance(ModAttributes.MAX_HUNGER).removeModifier(rLMaxHungerBuff);
        }

        if (foodConfig.sectionSaturation.ENABLE_SATURATION_CHANGES) {
            AttributeModifier modifier = new AttributeModifier(rLMaxSaturationBuff, this.max_saturation.getAddedValue(), ADD_VALUE);
            super.getPlayer().getAttributes().getInstance(ModAttributes.MAX_SATURATION).addOrReplacePermanentModifier(modifier);
        } else {
            attributeMap.getInstance(ModAttributes.MAX_SATURATION).removeModifier(rLMaxSaturationBuff);
        }

        if (foodConfig.sectionExhaustion.ENABLE_EXHAUSTION_CHANGES) {
            AttributeModifier modifier = new AttributeModifier(rLMaxExhaustionBuff, this.max_exhaustion.getAddedValue(), ADD_VALUE);
            super.getPlayer().getAttributes().getInstance(ModAttributes.MAX_EXHAUSTION).addOrReplacePermanentModifier(modifier);
        } else {
            attributeMap.getInstance(ModAttributes.MAX_EXHAUSTION).removeModifier(rLMaxExhaustionBuff);
        }

        /* TODO Fix Attribute part.

        if (ConfigAttributes.ENABLE_ARMOR_CHANGES.getAsBoolean()) {

            double amount_armor = PlayerValue.getAttributeValue(ConfigAttributes.ARMOR_PER_MILESTONE.getAsDouble(), ConfigAttributes.MILESTONES_FOR_ARMOR.get(), foodCount);
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
        } */

    }

    public static PlayerAttributes getOrCreatePlayerAttributes(Player player) {
        if (!PLAYER_ATTRIBUTES.containsKey(player.getUUID())) {
            int foodCount = FoodList.get(player).getProgressInfo().foodsEaten;
            PLAYER_ATTRIBUTES.put(player.getUUID(), new PlayerAttributes(player, foodCount));
        }
        return (PlayerAttributes) PlayerAttributes.getPlayerAttributes(player);
    }


}
