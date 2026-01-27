package backup.moddata.baseClases;

import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PlayerFoodInstance {

    private int foodCount = 0;
    private final Player player;

    private final List<MilestoneBased> MilestoneBasedList = new ArrayList<>();

    public PlayerFoodInstance(Player player, int foodCount) {
        this.player = player;
        this.foodCount = foodCount;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void updateFoodCount() {
        this.foodCount = 0;  //TODO FoodList.get(player).getProgressInfo().foodsEaten;
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


    /******************************************
     Enforce Behaivor
     ******************************************/

    protected void addMilestoneBased(MilestoneBased milestoneBased) {
        this.MilestoneBasedList.add(milestoneBased);
    }

    protected abstract void updateMilestones();

    public abstract List<? extends MilestoneBased> getMilestoneBasedList();

    public void updateBuffs() {
        updateBuffs(this.getFoodCount());
    }

    public abstract void updateBuffs(int foodCount);


    public Player getPlayer() {
        return this.player;
    }
}
