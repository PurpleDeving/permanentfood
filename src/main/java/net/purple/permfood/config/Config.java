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
            .comment("")
            .comment("When true, players will still get hungry on Peaceful difficulty. Set to false to disable hunger on Peaceful.")
            .define("hunger_on_peaceful", true);

    public static final ModConfigSpec.ConfigValue<Difficulty> PEACEFUL_HUNGER_DIFFICULTY = BUILDER
            .comment("")
            .comment("When hunger on Peaceful is enabled, this sets which difficulty's hunger rules to apply (EASY, NORMAL, or HARD).")
            .defineEnum("peaceful_hunger_difficulty", Difficulty.EASY, Difficulty.EASY, Difficulty.NORMAL, Difficulty.HARD);

    public static final ModConfigSpec.IntValue NATURAL_REGEN_THRESHOLD_WITH_SATURATION = BUILDER
            .comment("")
            .comment("Percentage of the hunger bar required for strong natural regeneration when the player has saturation (vanilla: 100).")
            .comment("Requires the game rule 'naturalRegeneration' to be enabled. Values are rounded down.")
            .defineInRange("natural_regen_threshold_with_saturation", 100, 0, 100);

    public static final ModConfigSpec.IntValue NATURAL_REGEN_THRESHOLD_NO_SATURATION = BUILDER
            .comment("")
            .comment("Percentage of the hunger bar required for weak natural regeneration when the player has no saturation (vanilla: 90).")
            .comment("Requires the game rule 'naturalRegeneration' to be enabled. Values are rounded down.")
            .defineInRange("natural_regen_threshold_no_saturation", 90, 0, 100);

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

    public static final ModConfigSpec.BooleanValue ENABLE_EXHAUSTION_CHANGES = BUILDER
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


    // TODO check both configs for the path names
    // TODO Default Values


    public static final ModConfigSpec SPEC = BUILDER.build();


}
