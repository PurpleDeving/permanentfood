package net.purple.permfood.config;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class ConfigStartup {

    // TODO  > Fix all the defaults
    // TODO > Check the path names

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();


    public static final ModConfigSpec.BooleanValue ENABLE_MAX_HUNGER_CHANGES = BUILDER
            .comment("")
            .comment("When true, this mod will modify the player's maximum hunger value.")
            .define("enable_max_hunger_changes", true);


    public static final ModConfigSpec.BooleanValue ENABLE_MAX_SATURATION_CHANGES = BUILDER
            .comment("")
            .comment("When true, this mod will modify the player's maximum saturation value.")
            .define("enable_max_saturation_changes", true);


    public static final ModConfigSpec.BooleanValue ENABLE_MAX_EXHAUSTION_CHANGES = BUILDER
            .comment("")
            .comment("When true, this mod will modify how exhaustion behaves for the player.")
            .define("enable_exhaustion_changes", true);


    // TODO > Add config screen ingame stuff


    // TODO check both configs for the path names
    // TODO Default Values


    public static final ModConfigSpec SPEC = BUILDER.build();


}
