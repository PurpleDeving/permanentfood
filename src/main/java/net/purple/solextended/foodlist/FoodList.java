package net.purple.solextended.foodlist;

import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.Set;

public class FoodList {

    public static Set<Item> ALLOWED_FOODS = new HashSet<>();
    // IMPL Populate ALLOWED_FOODS from configs
    // IMPL Redo on ConfigReload and ServerStart


    public static boolean isFoodAllowed() {
        // IMPL Parameter. Check if food is on big list
        return true;
    }

}
