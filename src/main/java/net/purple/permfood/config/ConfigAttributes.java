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

    // TODO > What other Buffs ? Max live? Max Hunger/Satuation? Ask GPT
    // TODO > Fix satuation writing everywhere

    // TODO > Add config screen ingame stuff


    //TODO - Add more Configs + Use Them


    //TODO > Add command to view all stats ingame + add them here.


    public static final ModConfigSpec SPEC = BUILDER.build();

}
