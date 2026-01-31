package net.purple.solextended.client.gui;

import net.minecraft.world.item.Item;
import net.purple.solextended.foodlist.PlayerFoodList;

import java.util.Set;

/**
 * Helper class that encapsulates player food tracking data.
 * Separates data logic from UI concerns.
 * Package-private - for internal use by the Food Book screen.
 */
final class FoodData {
    private final PlayerFoodList foodList;
    private final Set<Item> eatenFoods;

    FoodData(PlayerFoodList foodList) {
        this.foodList = foodList;
        this.eatenFoods = foodList.getEatenFoods();
    }

    /**
     * @return The number of unique foods eaten
     */
    int getFoodsEatenCount() {
        return eatenFoods.size();
    }

    /**
     * @return Read-only set of eaten foods
     */
    Set<Item> getEatenFoods() {
        return eatenFoods;
    }

    /**
     * @return The underlying PlayerFoodList
     */
    PlayerFoodList getFoodList() {
        return foodList;
    }
}
