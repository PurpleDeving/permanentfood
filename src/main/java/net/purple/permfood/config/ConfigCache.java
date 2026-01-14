package net.purple.permfood.config;

import net.minecraft.world.Difficulty;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;

import static net.purple.permfood.PermanentFood.MODID;

@EventBusSubscriber(modid = MODID)
public class ConfigCache {

    public static float EXHAUSTION_PER_HEAL;
    public static boolean HUNGER_ON_PEACEFUL_CACHED;
    public static Difficulty PEACEFUL_HUNGER_DIFFICULTY_CACHED;

    // enabler caches from Config
    public static boolean ENABLE_HUNGER_SCALING_CACHED;
    public static boolean ENABLE_EXHAUSTION_SCALING_CACHED;
    public static boolean ENABLE_SATURATION_SCALING_CACHED;

    // enabler caches from ConfigAttributes
    public static boolean ENABLE_ARMOR_CHANGES_CACHED;
    public static boolean ENABLE_ATTACK_DAMAGE_CHANGES_CACHED;
    public static boolean ENABLE_LUCK_CHANGES_CACHED;
    public static boolean ENABLE_KNOCKBACK_RESISTANCE_CHANGES_CACHED;


    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {
        reloadCache();
    }

    @SubscribeEvent
    public static void onConfigReLoad(ModConfigEvent.Reloading event) {
        reloadCache();
    }

    private static void reloadCache() {
        HUNGER_ON_PEACEFUL_CACHED = Config.HUNGER_ON_PEACEFUL.getAsBoolean();
        PEACEFUL_HUNGER_DIFFICULTY_CACHED = Config.PEACEFUL_HUNGER_DIFFICULTY.get();
        EXHAUSTION_PER_HEAL = (float) Config.EXHAUSTION_PER_HEAL.getAsDouble();

        // populate enablers from Config
        ENABLE_HUNGER_SCALING_CACHED = Config.ENABLE_MAX_HUNGER_CHANGES.getAsBoolean();
        ENABLE_EXHAUSTION_SCALING_CACHED = Config.ENABLE_EXHAUSTION_CHANGES.getAsBoolean();
        ENABLE_SATURATION_SCALING_CACHED = Config.ENABLE_MAX_SATURATION_CHANGES.getAsBoolean();

        // populate enablers from ConfigAttributes
        ENABLE_ARMOR_CHANGES_CACHED = ConfigAttributes.ENABLE_ARMOR_CHANGES.getAsBoolean();
        ENABLE_ATTACK_DAMAGE_CHANGES_CACHED = ConfigAttributes.ENABLE_ATTACK_DAMAGE_CHANGES.getAsBoolean();
        ENABLE_LUCK_CHANGES_CACHED = ConfigAttributes.ENABLE_LUCK_CHANGES.getAsBoolean();
        ENABLE_KNOCKBACK_RESISTANCE_CHANGES_CACHED = ConfigAttributes.ENABLE_KNOCKBACK_RESISTANCE_CHANGES.getAsBoolean();
    }


}
