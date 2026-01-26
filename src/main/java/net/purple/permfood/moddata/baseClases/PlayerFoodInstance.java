package net.purple.permfood.moddata.baseClases;

import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PlayerFoodInstance {

    private int foodCount = 0;
    private final Player player;

    private final List<MilestoneBased> MilestoneBasedList = new ArrayList<>();

    public PlayerFoodInstance(Player player) {
        this.player = player;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
        updateBuffs(foodCount);
    }

    protected List<? extends MilestoneBased> getRawMilestoneBasedList() {
        return MilestoneBasedList;
    }

    /**
     * Takes an in-memory snapshot of the current state of milestones.
     * Keyed by {@link MilestoneBased#getName()}.
     */
    public Map<String, Integer> snapshotMilestonesReached() {
        Map<String, Integer> snapshot = new HashMap<>();
        for (MilestoneBased milestoneBased : getMilestoneBasedList()) {
            snapshot.put(milestoneBased.getName(), milestoneBased.getMilestonesReached());
        }
        return snapshot;
    }

    /**
     * Compares two milestone snapshots and returns the stats that advanced.
     */
    public static List<MilestoneDiff> diffMilestones(Map<String, Integer> before, Map<String, Integer> after) {
        List<MilestoneDiff> diffs = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : after.entrySet()) {
            String name = entry.getKey();
            int afterValue = entry.getValue() == null ? 0 : entry.getValue();
            int beforeValue = before.getOrDefault(name, 0);
            if (afterValue > beforeValue) {
                diffs.add(new MilestoneDiff(name, beforeValue, afterValue));
            }
        }
        return diffs;
    }

    /******************************************
     Enforce Behaivor
     ******************************************/

    protected void addMilestoneBased(MilestoneBased milestoneBased) {
        this.MilestoneBasedList.add(milestoneBased);
    }

    protected abstract void updateMilestones();

    public abstract List<? extends MilestoneBased> getMilestoneBasedList();

    protected void updateBuffs() {
        updateBuffs(this.getFoodCount());
    }

    public abstract void updateBuffs(int foodCount);


    public Player getPlayer() {
        return this.player;
    }
}
