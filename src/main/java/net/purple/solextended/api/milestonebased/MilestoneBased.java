package net.purple.solextended.api.milestonebased;

import java.util.List;

public abstract class MilestoneBased {

    List<? extends Integer> milestones;
    private int currentMilestonesReached;
    private int previousMilestonesReached;
    private final String name;

    public MilestoneBased(String name, List<? extends Integer> milestones) {
        this.name = name;
        this.milestones = milestones;
    }

    /******************************************
     Information about the milestone list
     ******************************************/

    public List<? extends Integer> getMilestones() {
        return milestones;
    }

    public void updateMilestones(List<? extends Integer> milestones) {
        this.milestones = milestones;
    }

    /******************************************
     Information about the reached milestones
     ******************************************/

    public int getMilestonesReached() {
        return this.currentMilestonesReached;
    }

    public int getPreviousMilestonesReached() {
        return this.previousMilestonesReached;
    }

    public boolean maxMilestonesReached() {
        return this.currentMilestonesReached >= milestones.size();
    }

    private void updateMilestonesReached(int foodCount) {
        this.previousMilestonesReached = this.currentMilestonesReached;

        // TODO - Test if .length differs to .count()
        this.currentMilestonesReached = milestones.stream().mapToInt(Integer::intValue)
                .filter(milestone -> foodCount >= milestone).toArray().length;

    }

    /**
     * @return true if the milestone tier increased with this update
     */
    public boolean checkForReachedMilestone(int foodCount) {
        this.updateMilestonesReached(foodCount);
        return this.currentMilestonesReached > this.previousMilestonesReached;
    }

    /******************************************
     Information about the milestonebased thing
     ******************************************/

    public String getName() {
        return name;
    }


}
