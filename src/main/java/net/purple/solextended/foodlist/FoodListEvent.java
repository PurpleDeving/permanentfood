package net.purple.solextended.foodlist;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

public abstract class FoodListEvent extends Event {


    public static class PlayerFoodCountEvent extends FoodListEvent {

        private final Player player;
        private final int foodCount;


        public PlayerFoodCountEvent(Player player, int foodCount) {
            this.player = player;
            this.foodCount = foodCount;
        }

        public int getFoodCount() {
            return foodCount;
        }

        public Player getPlayer() {return player;}
    }


    // Event to signal that all player food values should be updated
    public static class UpdateAllPlayerValues extends FoodListEvent {
        
    }


}
// TODO make the stupid events here