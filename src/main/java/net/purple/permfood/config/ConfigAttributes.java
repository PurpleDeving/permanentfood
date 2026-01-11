package net.purple.permfood.config;

import com.google.common.collect.Lists;
import net.minecraft.world.Difficulty;
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
            .comment("To make some values (like Armor) work above certain threshold you will need a Mod like 'AttributeFix' to bypass Minecraft's attribute caps. \n ")
            .comment("See here for one mod option: https://www.curseforge.com/minecraft/mc-mods/attribute-fix")
            .comment("See here for more info: https://minecraft.fandom.com/wiki/Attribute")
            .comment("")
            .comment("")
            .comment("")
            .comment("Should the player get Armor from this mod?")
            .define("enable_armor_changes", true);

    public static final ModConfigSpec.DoubleValue ARMOR_PER_MILESTONE = BUILDER
            .comment("")
            .comment("The amount the player's armor is increased by per Milestone reached.")
            .defineInRange("armor_per_milestone", 2.0, 0.0, 1000.0);

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> MILESTONES_FOR_ARMOR = BUILDER
            .comment("")
            .comment("A list of numbers of unique foods you need to eat to unlock each milestone, in ascending order. Naturally, adding more milestones lets you earn more armor.")
            .defineList("milestones_armor", Lists.newArrayList(5, 10, 15, 20, 25), () -> 10, o -> o instanceof Integer);

    public static final ModConfigSpec.DoubleValue ARMOR_TOUGHNESS_PER_MILESTONE = BUILDER
            .comment("")
            .comment("The amount the player's armor toughness is increased by per Milestone reached.")
            .comment("If you are not sure what this is, look here: https://minecraft.fandom.com/wiki/Armor#Armor_toughness")
            .defineInRange("armor_toughness_per_milestone", 0.1, 0.0, 1000.0);

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> MILESTONES_FOR_ARMOR_TOUGHNESS = BUILDER
            .comment("")
            .comment("A list of numbers of unique foods you need to eat to unlock each milestone, in ascending order. Naturally, adding more milestones lets you earn more armor toughness.")
            .defineList("milestones_armor_toughness", Lists.newArrayList(5, 10, 15, 20, 25), () -> 10, o -> o instanceof Integer);

    /****************************************************************
     ATTACK DAMAGE BLOCK
     *****************************************************************/

    public static final ModConfigSpec.BooleanValue ENABLE_ATTACK_DAMAGE_CHANGES = BUILDER
            .comment("")
            .comment("Should the player get Attack Damage from this mod?")
            .define("enable_damage_changes", true);

    public static final ModConfigSpec.DoubleValue ATTACK_DAMAGE_PER_MILESTONE = BUILDER
            .comment("")
            .comment("The amount of additional damage the player deals by Milestone reached.")
            .comment("1.0 = Half a Heart")
            .defineInRange("damage_per_milestone", 1.0, 0.0, 1000.0);

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> MILESTONES_FOR_ATTACK_DAMAGE = BUILDER
            .comment("")
            .comment("A list of numbers of unique foods you need to eat to unlock each milestone, in ascending order. Naturally, adding more milestones lets you earn more armor.")
            .defineList("milestones_damage", Lists.newArrayList(5, 10, 15, 20, 25), () -> 10, o -> o instanceof Integer);

    /****************************************************************
     LUCK BLOCK
     *****************************************************************/

    public static final ModConfigSpec.BooleanValue ENABLE_LUCK_CHANGES = BUILDER
            .comment("")
            .comment("Should the player get Luck from this mod?")
            .define("enable_luck_changes", true);

    public static final ModConfigSpec.DoubleValue LUCK_PER_MILESTONE = BUILDER
            .comment("")
            .comment("The amount of Luck the player gets for each Milestone reached.")
            .comment("1.0 = Half a Heart")
            .defineInRange("damage_per_milestone", 1.0, -100, 1000.0);

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> MILESTONES_FOR_LUCK = BUILDER
            .comment("")
            .comment("A list of numbers of unique foods you need to eat to unlock each milestone, in ascending order. Naturally, adding more milestones lets you earn more luck.")
            .defineList("milestones_luck", Lists.newArrayList(5, 10, 15, 20, 25), () -> 10, o -> o instanceof Integer);

    // TODO > What other Buffs ? Max live? Max Hunger/Satuation? Ask GPT
    // TODO > Fix satuation writing everywhere

    // TODO > Add config screen ingame stuff


    //TODO - Add more Configs + Use Them


    //TODO > Add command to view all stats ingame + add them here.


    public static final ModConfigSpec SPEC = BUILDER.build();

}
