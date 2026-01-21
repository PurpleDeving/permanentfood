package net.purple.permfood.config;

import com.google.common.collect.Lists;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class ConfigAttributes {

    // TODO  > Fix all the defaults
    // TODO > Check the path names

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();


    /****************************************************************
     ARMOR BLOCK
     *****************************************************************/

    public static final ModConfigSpec.BooleanValue ENABLE_ARMOR_CHANGES = BUILDER
            .comment("Some attribute caps in Minecraft may prevent values above a threshold; use an attribute-cap fix mod if needed.")
            .comment("See: https://minecraft.fandom.com/wiki/Attribute for info and https://www.curseforge.com/minecraft/mc-mods/attribute-fix for one mod  option.")
            .comment("")
            .comment("When true, this mod will grant armor attribute increases to the player based on milestones.")
            .define("enable_armor_changes", true);

    public static final ModConfigSpec.DoubleValue ARMOR_PER_MILESTONE = BUILDER
            .comment("")
            .comment("Amount of armor value added to the player for each milestone reached.")
            .defineInRange("armor_per_milestone", 2.0, 0.0, 1000.0);

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> MILESTONES_FOR_ARMOR = BUILDER
            .comment("")
            .comment("Ascending list of numbers of unique foods required to reach each armor milestone.")
            .defineList("milestones_for_armor", Lists.newArrayList(5, 10, 15, 20, 25), () -> 10, o -> o instanceof Integer);

    public static final ModConfigSpec.DoubleValue ARMOR_TOUGHNESS_PER_MILESTONE = BUILDER
            .comment("")
            .comment("Amount of armor toughness added to the player for each milestone reached.")
            .comment("If unfamiliar, see: https://minecraft.fandom.com/wiki/Armor#Armor_toughness")
            .defineInRange("armor_toughness_per_milestone", 0.1, 0.0, 1000.0);

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> MILESTONES_FOR_ARMOR_TOUGHNESS = BUILDER
            .comment("")
            .comment("Ascending list of numbers of unique foods required to reach each armor-toughness milestone.")
            .defineList("milestones_for_armor_toughness", Lists.newArrayList(5, 10, 15, 20, 25), () -> 10, o -> o instanceof Integer);

    /****************************************************************
     ATTACK DAMAGE BLOCK
     *****************************************************************/

    public static final ModConfigSpec.BooleanValue ENABLE_ATTACK_DAMAGE_CHANGES = BUILDER
            .comment("")
            .comment("When true, this mod will grant attack damage increases to the player based on milestones.")
            .define("enable_attack_damage_changes", true);

    public static final ModConfigSpec.DoubleValue ATTACK_DAMAGE_PER_MILESTONE = BUILDER
            .comment("")
            .comment("Amount of attack damage added to the player for each milestone reached. 1.0 equals half a heart.")
            .defineInRange("attack_damage_per_milestone", 1.0, 0.0, 1000.0);

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> MILESTONES_FOR_ATTACK_DAMAGE = BUILDER
            .comment("")
            .comment("Ascending list of numbers of unique foods required to reach each attack-damage milestone.")
            .defineList("milestones_for_attack_damage", Lists.newArrayList(5, 10, 15, 20, 25), () -> 10, o -> o instanceof Integer);

    /****************************************************************
     LUCK BLOCK
     *****************************************************************/

    public static final ModConfigSpec.BooleanValue ENABLE_LUCK_CHANGES = BUILDER
            .comment("")
            .comment("When true, this mod will grant Luck increases to the player based on milestones.")
            .define("enable_luck_changes", true);

    public static final ModConfigSpec.DoubleValue LUCK_PER_MILESTONE = BUILDER
            .comment("")
            .comment("Amount of Luck added to the player for each milestone reached. Positive values increase luck, negative decrease it.")
            .defineInRange("luck_per_milestone", 1.0, -100.0, 1000.0);

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> MILESTONES_FOR_LUCK = BUILDER
            .comment("")
            .comment("Ascending list of numbers of unique foods required to reach each luck milestone.")
            .defineList("milestones_for_luck", Lists.newArrayList(5, 10, 15, 20, 25), () -> 10, o -> o instanceof Integer);

    /****************************************************************
     KNOCKBACK RESISTANCE BLOCK
     *****************************************************************/

    public static final ModConfigSpec.BooleanValue ENABLE_KNOCKBACK_RESISTANCE_CHANGES = BUILDER
            .comment("")
            .comment("When true, this mod will grant knockback resistance increases to the player based on milestones.")
            .define("enable_knockback_resistance_changes", true);

    public static final ModConfigSpec.DoubleValue KNOCKBACK_RESISTANCE_PER_MILESTONE = BUILDER
            .comment("")
            .comment("Amount of generic_knockback_resistance added to the player for each milestone reached. Values should be in the range [0.0, 1.0] for percent-like resistance.")
            .defineInRange("knockback_resistance_per_milestone", 0.02, 0.0, 1.0);

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> MILESTONES_FOR_KNOCKBACK_RESISTANCE = BUILDER
            .comment("")
            .comment("Ascending list of numbers of unique foods required to reach each knockback-resistance milestone.")
            .defineList("milestones_for_knockback_resistance", Lists.newArrayList(5, 10, 15, 20, 25), () -> 10, o -> o instanceof Integer);


    /****************************************************************
     HUNGER SCALING BLOCK
     *****************************************************************/

    public static final ModConfigSpec.BooleanValue ENABLE_MAX_HUNGER_CHANGES = BUILDER
            .comment("")
            .comment("When true, this mod will modify the player's maximum hunger value.")
            .define("enable_max_hunger_changes", true);

    public static final ModConfigSpec.IntValue NEW_BASE_MAX_HUNGER = BUILDER
            .comment("")
            .comment("Base maximum hunger value that food can fill. Vanilla default is 20.")
            .defineInRange("new_base_max_hunger", 20, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue MAX_HUNGER_PER_MILESTONE = BUILDER
            .comment("")
            .comment("Amount added to the player's maximum hunger for each milestone reached.")
            .defineInRange("max_hunger_per_milestone", 2, 0, 1000);

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> MILESTONES_FOR_MAX_HUNGER = BUILDER
            .comment("")
            .comment("Ascending list of numbers of unique foods required to reach each maximum-hunger milestone.")
            .defineList("milestones_for_max_hunger", Lists.newArrayList(5, 10, 15, 20, 25), () -> 10, o -> o instanceof Integer);


    /****************************************************************
     SATURATION BLOCK
     *****************************************************************/

    public static final ModConfigSpec.BooleanValue ENABLE_MAX_SATURATION_CHANGES = BUILDER
            .comment("")
            .comment("When true, this mod will modify the player's maximum saturation value.")
            .define("enable_max_saturation_changes", true);

    public static final ModConfigSpec.DoubleValue NEW_BASE_MAX_SATURATION = BUILDER
            .comment("")
            .comment("Base maximum saturation value that food can fill. Vanilla default is 20.0.")
            .defineInRange("new_base_max_saturation", 20.0, 0.1, Float.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue MAX_SATURATION_PER_MILESTONE = BUILDER
            .comment("")
            .comment("Amount added to the player's maximum saturation for each milestone reached.")
            .defineInRange("max_saturation_per_milestone", 2.0, 0.0, 1000.0);

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> MILESTONES_FOR_MAX_SATURATION = BUILDER
            .comment("")
            .comment("Ascending list of numbers of unique foods required to reach each maximum-saturation milestone.")
            .defineList("milestones_for_max_saturation", Lists.newArrayList(5, 10, 15, 20, 25), () -> 10, o -> o instanceof Integer);


    /****************************************************************
     Exhaustion BLOCK
     *****************************************************************/

    public static final ModConfigSpec.BooleanValue ENABLE_MAX_EXHAUSTION_CHANGES = BUILDER
            .comment("")
            .comment("When true, this mod will modify how exhaustion behaves for the player.")
            .define("enable_exhaustion_changes", true);

    public static final ModConfigSpec.DoubleValue NEW_BASE_MAX_EXHAUSTION = BUILDER
            .comment("")
            .comment("Base exhaustion threshold before food is consumed. Higher values reduce food loss. Vanilla default is 4.0.")
            .defineInRange("new_base_max_exhaustion", 4.0, 0.1, Float.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue MAX_EXHAUSTION_PER_MILESTONE = BUILDER
            .comment("")
            .comment("Amount added to the exhaustion threshold for each milestone reached. Higher values reduce food loss.")
            .defineInRange("max_exhaustion_per_milestone", 0.1, 0.0, 1000.0);

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> MILESTONES_FOR_MAX_EXHAUSTION = BUILDER
            .comment("")
            .comment("Ascending list of numbers of unique foods required to reach each exhaustion milestone.")
            .defineList("milestones_for_max_exhaustion", Lists.newArrayList(5, 10, 15, 20, 25), () -> 10, o -> o instanceof Integer);

    public static final ModConfigSpec.DoubleValue EXHAUSTION_PER_HEAL = BUILDER
            .comment("")
            .comment("Amount of exhaustion applied to the player when they heal one heart. Higher values increase exhaustion cost for healing. Vanilla default is 6.0.")
            .defineInRange("exhaustion_per_heal", 6.0, 0.1, 1000.0);


    // TODO > Add config screen ingame stuff


    //TODO - Add more Configs + Use Them


    //TODO > Add command to view all stats ingame + add them here.


    public static final ModConfigSpec SPEC = BUILDER.build();

}
