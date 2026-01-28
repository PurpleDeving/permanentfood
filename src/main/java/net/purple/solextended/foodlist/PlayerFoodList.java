package net.purple.solextended.foodlist;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PlayerFoodList implements INBTSerializable<CompoundTag> {
    private static final String NBT_KEY_FOOD_LIST = "foodList";

    //IMPL Set of all eaten foods for that player.
    private final Set<Item> eatenFoods = new HashSet<>();


    public PlayerFoodList() {
    }

    public int getFoodEatenCount(String foodId) {
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
    private boolean addFood(Item item) {
        if (FoodList.isFoodAllowed(item)) {
            return eatenFoods.add(item);
        }

        return false;
    }

    /******************************************
     Network
     ******************************************/

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        var tag = new CompoundTag();

        var list = new ListTag();
        eatenFoods.stream()
                .map(Item::encode)
                .filter(Objects::nonNull)
                .map(StringTag::valueOf)
                .forEach(list::add);
        tag.put(NBT_KEY_FOOD_LIST, list);

        return tag;
    }

    /**
     * used for persistent storage
     */
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        var list = tag.getList(NBT_KEY_FOOD_LIST, Tag.TAG_STRING);

        eatenFoods.clear();
        list.stream()
                .map(nbt -> (StringTag) nbt)
                .map(StringTag::getAsString)
                .map(Item::decode)
                .filter(Objects::nonNull)
                .forEach(eatenFoods::add);

    }

    //IMPL Persistent Storage. On deserialize, check all food to be allowed

    // IMPLS Boolean if food has been eaten

    // Impl Add food should be boolean and return true if new food
}
