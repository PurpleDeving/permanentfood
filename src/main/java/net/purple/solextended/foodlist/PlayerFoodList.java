package net.purple.solextended.foodlist;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class PlayerFoodList {

    //IMPL Set of all eaten foods for that player.
    private final Set<Item> eatenFoods = new HashSet<>();


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

    //IMPL Persistent Storage. On deserialize, check all food to be allowed

    // IMPLS Boolean if food has been eaten

    // Impl Add food should be boolean and return true if new food
}
