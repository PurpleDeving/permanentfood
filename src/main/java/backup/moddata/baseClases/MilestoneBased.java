package backup.moddata.baseClases;

import java.util.List;

public abstract class MilestoneBased {

    List<? extends Integer> milestones;
    int milestonesReached;

    /**
     * Previous milestone count from the last {@link #updateMilestonesReached(int)} call.
     */
    int previousMilestonesReached;

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

    /**
     * @return milestone count before the last update.
     */
    public int getPreviousMilestonesReached() {
        return this.previousMilestonesReached;
    }

    public void updateMilestones(List<? extends Integer> milestones) {
        this.milestones = milestones;
    }

    public String getName() {
        return name;
    }

    /**
     * Updates the internal milestone counter based on the provided food count.
     * Also stores the previous milestone count so callers can compute deltas.
     */
    public int updateMilestonesReached(int foodCount) {
        this.previousMilestonesReached = this.milestonesReached;

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


    /**
     * @return true if the milestone tier increased in the last update.
     */
    public boolean advancedMilestone() {
        return this.milestonesReached > this.previousMilestonesReached;
    }

    public boolean maxMilestonesReached() {
        return milestonesReached >= milestones.size();
    }

}
