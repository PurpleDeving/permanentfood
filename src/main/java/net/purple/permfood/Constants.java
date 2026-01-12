package net.purple.permfood;


import net.minecraft.resources.ResourceLocation;

import static net.purple.permfood.PermanentFood.MODID;

public class Constants {

    // TODO FILL WITH USEFULL STUFF THIS TIME
    public static final String SOL_CARROT = "solcarrot";

    public static final int VANILLA_MAX_HUNGER = 20;
    public static final float VANILLA_MAX_SATURATION = 20.0F;
    public static final float VANILLA_MAX_EXHAUSTION = 4.0F;
    public static final String MOD_AUTHOR_LONG = "Purple Dev";

    public static ResourceLocation resourceLocationFoodArmorBuff = ResourceLocation.fromNamespaceAndPath(MODID, "food_armor_buff");
    public static ResourceLocation resourceLocationFoodArmorToughnessBuff = ResourceLocation.fromNamespaceAndPath(MODID, "food_armor_toughness_buff");
    public static ResourceLocation resourceLocationFoodAttackDamageBuff = ResourceLocation.fromNamespaceAndPath(MODID, "food_attack_damage_buff");
    public static ResourceLocation resourceLocationFoodLuckBuff = ResourceLocation.fromNamespaceAndPath(MODID, "food_luck_buff");
    public static ResourceLocation resourceLocationFoodKnockbackResistanceBuff = ResourceLocation.fromNamespaceAndPath(MODID, "food_knockback_resistance_buff");

}
