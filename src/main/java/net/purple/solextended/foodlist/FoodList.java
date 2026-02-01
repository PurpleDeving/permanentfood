package net.purple.solextended.foodlist;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.purple.solextended.SolExtended;
import net.purple.solextended.config.Configs;
import net.purple.solextended.config.SolExtendedConfig;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FoodList {

    private static Set<Item> ALLOWED_FOODS;

    public static Set<Item> lazzyGetAllowedFoods() {
        if (ALLOWED_FOODS == null) {
            ALLOWED_FOODS = new HashSet<>();
            updateAllowedFoods();
        }
        return ALLOWED_FOODS;
    }

    public static void updateAllowedFoods() {
        // Build a fresh set locally then swap it in to minimize race windows and work on the server thread.
        Set<Item> newSet = new HashSet<>();

        var config = Configs.solExtendedConfig;

        // If whitelist contains entries, prefer iterating only over those (much faster for large registries)
        if (config.listMode.get() == SolExtendedConfig.ListMode.WHITELIST && !config.whiteList.isEmpty()) {
            for (ResourceLocation rl : config.whiteList) {
                try {
                    Item item = BuiltInRegistries.ITEM.get(rl);
                    // Registry lookups never return null; unknown ids resolve to the default (AIR).
                    if (item == Items.AIR) continue;
                    if (!isFood(item)) continue;
                    if (!isHealthy(item)) continue;

                    newSet.add(item);

                } catch (Throwable ignored) {
                    SolExtended.LOGGER.warn("Failed to resolve whitelisted item: {}", rl);
                }
            }
        } else {

            Set<Item> blacklistedItems = new HashSet<>();
            if (config.listMode.get() == SolExtendedConfig.ListMode.BLACKLIST && !config.blackList.isEmpty()) {
                for (ResourceLocation rl : config.blackList) {
                    try {
                        Item item = BuiltInRegistries.ITEM.get(rl);
                        // Registry lookups never return null; unknown ids resolve to the default (AIR).
                        if (item != Items.AIR) {
                            blacklistedItems.add(item);
                        }
                    } catch (Throwable ignored) {
                        SolExtended.LOGGER.warn("Failed to resolve blacklisted item: {}", rl);
                    }
                }
            }

            BuiltInRegistries.ITEM.stream().forEach(item -> {
                try {
                    if (item == Items.AIR) return;
                    if (blacklistedItems.contains(item)) return;
                    if (!isFood(item)) return;
                    if (!isHealthy(item)) return;
                    newSet.add(item);
                } catch (Throwable ignoredInner) {
                    // ignore corrupt entries
                }
            });

        }
        ALLOWED_FOODS = Collections.unmodifiableSet(newSet);
    }

    public static boolean isHealthy(Item item) {
        if (!isFood(item)) return false;
        //noinspection DataFlowIssue
        return item.getFoodProperties(item.getDefaultInstance(), null).saturation() >= Configs.solExtendedConfig.minimumFoodValue.get();
    }

    public static boolean isFoodAllowed(Item item) {
        return lazzyGetAllowedFoods().contains(item);
    }

    public static boolean isFood(Item item) {
        FoodProperties foodProps = item.getFoodProperties(item.getDefaultInstance(), null);
        return foodProps != null;
    }


    // IMPL Redo on ConfigReload and ServerStart


}
