package net.purple.permfood.moddata.baseClases;

import java.util.List;

public abstract class MilestoneBased {

    List<? extends Integer> milestones;
    int milestonesReached;
    private String name;

    public MilestoneBased(String name) {
        this.name = name;
    }

    public List<? extends Integer> getMilestones() {
        return milestones;
    }

    public int getMilestonesReached() {
        return this.milestonesReached;
    }

    public void updateMilestones(List<? extends Integer> milestones) {
        this.milestones = milestones;
    }

    public String getName() {
        return name;
    }

    public int updateMilestonesReached(int foodCount) {
        int reachedMilestones = 0;

        for (int milestone : this.milestones) {
            if (foodCount >= milestone) {
                reachedMilestones++;
            } else {
                break;
            }
        }
        this.milestonesReached = reachedMilestones;

        return reachedMilestones;
    }

    public boolean maxMilestonesReached() {
        return milestonesReached >= milestones.size();
    }

}
