package net.purple.solextended.foodlist;

import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.Set;

public class FoodList {

    private static Set<Item> ALLOWED_FOODS;

    public Set<Item> lazzyGetAllowedFoods() {
        if (ALLOWED_FOODS == null) {
            ALLOWED_FOODS = new HashSet<>();
            updateAllowedFoods();
        }
        return ALLOWED_FOODS;
    }

    public void updateAllowedFoods() {
        // IMPL Populate ALLOWED_FOODS with foods that are not on the blacklist (or are only on the whitelist, if whitelist has entries). Also check minimum food value.
    }


    // IMPL Populate ALLOWED_FOODS from configs
    // IMPL Redo on ConfigReload and ServerStart


    public static boolean isFoodAllowed() {
        // IMPL Parameter. Check if food is on big list
        return true;
    }

}
