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

}
// TODO make the stupid events here