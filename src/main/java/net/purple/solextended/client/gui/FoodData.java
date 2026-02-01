package net.purple.solextended.client.gui;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.purple.solextended.foodlist.PlayerFoodList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.purple.solextended.Constants.FOOD_BOOK_TEST_ITEMS;

/**
 * Helper class that encapsulates player food tracking data.
 * Separates data logic from UI concerns.
 * Package-private - for internal use by the Food Book screen.
 */
@OnlyIn(Dist.CLIENT)
final class FoodData {
    private final PlayerFoodList foodList;
    private final Set<Item> eatenFoods;

    FoodData(PlayerFoodList foodList) {
        this.foodList = foodList;

        // In dev mode, add test items for GUI testing
        if (FOOD_BOOK_TEST_ITEMS) {
            Set<Item> testFoods = new HashSet<>(foodList.getEatenFoods());

            // Collect all food items from registry
            List<Item> allFoodItems = BuiltInRegistries.ITEM.stream()
                    .filter(item -> item.getFoodProperties(item.getDefaultInstance(), null) != null)
                    .toList();
            testFoods.addAll(allFoodItems);

            this.eatenFoods = testFoods;
        } else {
            this.eatenFoods = foodList.getEatenFoods();
        }
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
