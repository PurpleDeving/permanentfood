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
    }


}
