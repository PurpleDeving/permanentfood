package net.purple.permfood.config;

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


    // TODO > Add config screen ingame stuff


    // TODO check both configs for the path names
    // TODO Default Values


    public static final ModConfigSpec SPEC = BUILDER.build();


}
