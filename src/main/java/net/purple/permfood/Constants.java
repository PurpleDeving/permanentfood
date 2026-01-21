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

    public static ResourceLocation rLMaxHungerBuff = ResourceLocation.fromNamespaceAndPath(MODID, "max_hunger_buff");
    public static ResourceLocation rLMaxSaturationBuff = ResourceLocation.fromNamespaceAndPath(MODID, "max_saturation_buff");
    public static ResourceLocation rLMaxExhaustionBuff = ResourceLocation.fromNamespaceAndPath(MODID, "max_exhaustion_buff");
    public static ResourceLocation rLArmorBuff = ResourceLocation.fromNamespaceAndPath(MODID, "food_armor_buff");
    public static ResourceLocation rLToughnessBuff = ResourceLocation.fromNamespaceAndPath(MODID, "food_armor_toughness_buff");
    public static ResourceLocation rLAttackDamageBuff = ResourceLocation.fromNamespaceAndPath(MODID, "food_attack_damage_buff");
    public static ResourceLocation rLLuckBuff = ResourceLocation.fromNamespaceAndPath(MODID, "food_luck_buff");
    public static ResourceLocation rLKnockbackResistanceBuff = ResourceLocation.fromNamespaceAndPath(MODID, "food_knockback_resistance_buff");

}
