package net.purple.permfood.config;

import java.util.List;

import com.google.common.collect.Lists;
import net.minecraft.world.Difficulty;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {

    // TODO  > Fix all the defaults
    // TODO > Check the path names

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    /****************************************************************
     HUNGER MISC
     *****************************************************************/

    public static final ModConfigSpec.BooleanValue HUNGER_ON_PEACEFUL = BUILDER
            .comment("Should you get hungry on peaceful? Highly recommended with this mods setup.")
            .define("bool_hunger_on_peaceful", true);

    public static final ModConfigSpec.ConfigValue<Difficulty> PEACEFUL_HUNGER_DIFFICULTY = BUILDER
            .comment("")
            .comment("Gets ignored when HUNGER_ON_PEACEFUL is false. Sets the desired difficulty for the hunger mechanic when on Peaceful difficulty.")
            .defineEnum("peaceful_hunger_difficulty", Difficulty.EASY, new Difficulty[]{Difficulty.EASY, Difficulty.NORMAL, Difficulty.HARD});

    public static final ModConfigSpec.IntValue NATURAL_REGEN_THRESHOLD_WITH_SATURATION = BUILDER
            .comment("")
            .comment("How much food is needed for strong Natural Regeneration. Vanilla is 100%")
            .comment("Works only with the game rule: RULE_NATURAL_REGENERATION. Works only when you have saturation [Unchanged].")
            .comment("Resulting values are rounded down.")
            .defineInRange("natural_regen_threshold", 100, 0, 100);

    public static final ModConfigSpec.IntValue NATURAL_REGEN_THRESHOLD_NO_SATURATION = BUILDER
            .comment("")
            .comment("How much food is needed for weak Natural Regeneration. Vanilla is 90%")
            .comment("Works only with the game rule: RULE_NATURAL_REGENERATION. Works with 0 saturation [Unchanged].")
            .comment("Resulting values are rounded down.")
            .defineInRange("non_natural_regen_threshold", 90, 0, 100);

    /****************************************************************
     HUNGER SCALING BLOCK
     *****************************************************************/

    public static final ModConfigSpec.BooleanValue ENABLE_MAX_HUNGER_CHANGES = BUILDER
            .comment("")
            .comment("Should the maximum hunger amount be changed by this mod?")
            .define("enable_max_hunger", true);

    public static final ModConfigSpec.IntValue NEW_BASE_MAX_HUNGER = BUILDER
            .comment("")
            .comment("The new maximum base amount of hunger food can fill.")
            .comment("Vanilla default: 20")
            .defineInRange("new_base_max_hunger", 20, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue MAX_HUNGER_PER_MILESTONE = BUILDER
            .comment("")
            .comment("The amount the maximum hunger is increased by per Milestone reached.")
            .defineInRange("max_hunger_per_milestone", 2, 0, 1000);

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> MILESTONES_FOR_MAX_HUNGER = BUILDER
            .comment("")
            .comment("A list of numbers of unique foods you need to eat to unlock each milestone, in ascending order. Naturally, adding more milestones lets you earn more max hunger.")
            .defineList("milestones_hunger", Lists.newArrayList(5, 10, 15, 20, 25), o -> o instanceof Integer);


    /****************************************************************
     SATURATION BLOCK
     *****************************************************************/

    public static final ModConfigSpec.BooleanValue ENABLE_MAX_SATURATION_CHANGES = BUILDER
            .comment("")
            .comment("Should the maximum saturation amount be changed by this mod?")
            .define("enable_max_saturation", true);

    public static final ModConfigSpec.DoubleValue NEW_BASE_MAX_SATURATION = BUILDER
            .comment("")
            .comment("The new maximum base amount of saturation food can fill.")
            .comment("Vanilla default: 20.0")
            .defineInRange("new_base_max_saturation", 20.0, 0.1, Float.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue MAX_SATURATION_PER_MILESTONE = BUILDER
            .comment("")
            .comment("The amount the maximum saturation is increased by per Milestone reached.")
            .defineInRange("max_saturation_per_milestone", 2.0, 0.0, 1000.0);

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> MILESTONES_FOR_MAX_SATURATION = BUILDER
            .comment("")
            .comment("A list of numbers of unique foods you need to eat to unlock each milestone, in ascending order. Naturally, adding more milestones lets you earn more max saturation.")
            .defineList("milestones_saturation", Lists.newArrayList(5, 10, 15, 20, 25), o -> o instanceof Integer);


    /****************************************************************
     Exhaustion BLOCK
     *****************************************************************/

    public static final ModConfigSpec.BooleanValue ENABLE_EXHAUSTION_CHANGES = BUILDER
            .comment("")
            .comment("Should the exhaustion get changed by this mod?")
            .define("enable_exhaustion", true);

    public static final ModConfigSpec.DoubleValue NEW_BASE_MAX_EXHAUSTION = BUILDER
            .comment("")
            .comment("The new maximum base amount of exhaustion the player can have before food is consumed.")
            .comment("A higher number will result in less food loss.")
            .comment("Vanilla default: 4.0")
            .defineInRange("new_base_max_exhaustion", 4.0, 0.1, Float.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue MAX_EXHAUSTION_PER_MILESTONE = BUILDER
            .comment("")
            .comment("The amount the maximum exhaustion is increased by per Milestone reached.")
            .comment("A higher number will result in less food loss.")
            .defineInRange("max_exhaustion_per_milestone", 0.1, 0.0, 1000.0);

    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> MILESTONES_FOR_MAX_EXHAUSTION = BUILDER
            .comment("")
            .comment("A list of numbers of unique foods you need to eat to unlock each milestone, in ascending order. Naturally, adding more milestones lets you earn more max exhaustion.")
            .defineList("milestones_exhaustion", Lists.newArrayList(5, 10, 15, 20, 25), o -> o instanceof Integer);

    public static final ModConfigSpec.DoubleValue EXHAUSTION_PER_HEAL = BUILDER
            .comment("")
            .comment("The new amount of exhaustion the player gets for healing 1 heart. Higher is worse.")
            .comment("Vanilla default: 6.0")
            .defineInRange("new_base_max_saturation", 6.0, 0.1, 1000.0);

    /****************************************************************
     ARMOR BLOCK
     *****************************************************************/


    public static final ModConfigSpec.DoubleValue UNIQUE_FOOD_SCALING_ARMOR = BUILDER
            .comment("")
            .comment("Define how much additional armour a player should get for each unique food the player ate.")
            .comment("0.0 does disable the scaling.")
            .defineInRange("scaling_armour", 0.0, 0.0, 100.0);


    // TODO > What other Buffs ? Max live? Max Hunger/Satuation? Ask GPT
    // TODO > Fix satuation writing everywhere

    // TODO > Add config screen ingame stuff


    //TODO - Add more Configs + Use Them


    //TODO > Add command to view all stats ingame + add them here.


    // I think this is obsolet???
    public static final ModConfigSpec.BooleanValue NATURAL_REGEN_ON_PEACEFUL = BUILDER
            .comment("")
            .comment("Should the vanilla Interaction with the natural_health_regeneration on peaceful be enabled? Highly recommended to keep this disabled.")
            .define("natural_regen_on_peaceful", false);


    public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("")
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
/*    public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), () -> "", Config::validateItemName);*/


    public static final ModConfigSpec SPEC = BUILDER.build();


/*    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }*/
}
