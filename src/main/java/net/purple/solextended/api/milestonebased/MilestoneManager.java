package net.purple.solextended.api.milestonebased;

import net.neoforged.bus.api.SubscribeEvent;
import net.purple.solextended.foodlist.FoodListEvent;

import java.util.HashSet;
import java.util.Set;

public abstract class MilestoneManager {

    final Set<MilestoneProgression> milestoneProgressions = new HashSet<>();

    @SubscribeEvent
    public abstract void onFoodCoundUpdate(FoodListEvent.PlayerFoodCountEvent event);


}
