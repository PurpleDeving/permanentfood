package net.purple.solextended.api.milestonebased;

import net.neoforged.bus.api.SubscribeEvent;
import net.purple.solextended.foodlist.FoodListEvent;

import java.util.HashSet;
import java.util.Set;

public abstract class MilestoneManager<T extends MilestoneProgression> {

    private final Set<T> milestoneProgressions = new HashSet<>();

    public Set<T> getMilestoneProgressions() {
        return milestoneProgressions;
    }

    @SubscribeEvent
    public abstract void onFoodCoundUpdate(FoodListEvent.PlayerFoodCountEvent event);


}
