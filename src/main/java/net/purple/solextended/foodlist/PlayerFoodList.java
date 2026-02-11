package net.purple.solextended.foodlist;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashSet;
import java.util.Set;

public class PlayerFoodList implements INBTSerializable<CompoundTag> {
    private static final String NBT_KEY_FOOD_LIST = "foodList";

    private final Set<Item> eatenFoods = new HashSet<>();


    public PlayerFoodList() {
    }

    public int getFoodEatenCount() {
        return eatenFoods.size();
    }

    /**
     * @return true if the food was not previously known, i.e. if a new food has been tried
     */
    public boolean addFood(ItemStack food) {

        return addFood(food.getItem());
    }

    /**
     * @return true if the food was not previously known, i.e. if a new food has been tried
     */
    public boolean addFood(Item item) {
        if (FoodList.isFoodAllowed(item)) {
            return eatenFoods.add(item);
        }

        return false;
    }

    /**
     * Read-only view of all foods this player has eaten.
     */
    public Set<Item> getEatenFoods() {
        return java.util.Collections.unmodifiableSet(eatenFoods);
    }

    public boolean hasEaten(Item food) {
        return eatenFoods.contains(food);
    }

    public void clearList() {
        eatenFoods.clear();
    }


    /******************************************
     Network / Persistent storage
     ******************************************/

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        var tag = new CompoundTag();

        // Store as registry ids (e.g. "minecraft:apple").
        var list = new ListTag();
        eatenFoods.stream()
                .map(BuiltInRegistries.ITEM::getKey)
                .filter(java.util.Objects::nonNull)
                .map(ResourceLocation::toString)
                .map(StringTag::valueOf)
                .forEach(list::add);
        tag.put(NBT_KEY_FOOD_LIST, list);

        return tag;
    }

    /**
     * Used for persistent storage.
     * On load we re-check FoodList.isFoodAllowed so config changes can invalidate stored entries.
     */
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        var list = tag.getList(NBT_KEY_FOOD_LIST, Tag.TAG_STRING);

        eatenFoods.clear();
        for (int i = 0; i < list.size(); i++) {
            String id = list.getString(i);
            ResourceLocation rl = ResourceLocation.tryParse(id);
            if (rl == null) continue;

            Item item;
            try {
                item = BuiltInRegistries.ITEM.get(rl);
            } catch (Throwable ignored) {
                continue;
            }

            // Unknown ids resolve to AIR in the builtin registries.
            if (item == Items.AIR) continue;

            if (FoodList.isFoodAllowed(item)) {
                eatenFoods.add(item);
            }
        }

    }


}
