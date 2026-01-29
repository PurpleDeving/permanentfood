package net.purple.solextended.foodlist;

import net.neoforged.bus.api.Event;

public abstract class FoodListEvent extends Event {


    public static class PlayerFoodCountEvent extends FoodListEvent {

        private final int foodCount;

        public PlayerFoodCountEvent(int foodCount) {
            this.foodCount = foodCount;
        }

        public int getFoodCount() {
            return foodCount;
        }

    }


    // Event to signal that all player food values should be updated
    public static class UpdateAllPlayerValues extends FoodListEvent {
    }


}
// TODO make the stupid events here