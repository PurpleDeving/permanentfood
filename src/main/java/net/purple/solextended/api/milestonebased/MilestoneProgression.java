package net.purple.solextended.api.milestonebased;

import java.util.List;
import java.util.Objects;

/**
 * Per-instance milestone tracking (per player/thing).
 *
 * <p>References a shared {@link MilestoneType} which owns the milestone list.
 * This class only stores current/previous reached tiers.</p>
 */
public final class MilestoneProgression {

    private final MilestoneType type;

    private int currentMilestonesReached;
    private int previousMilestonesReached;

    public MilestoneProgression(MilestoneType type) {
        this.type = Objects.requireNonNull(type, "type");
    }

    public MilestoneType getType() {
        return type;
    }

    public String getName() {
        return type.getName();
    }

    /******************************************
     Information about the number of milestones reached
     ******************************************/

    public List<Integer> getMilestones() {
        return type.getMilestones();
    }

    public int getMilestonesReached() {
        return currentMilestonesReached;
    }

    public boolean maxMilestonesReached() {
        return currentMilestonesReached >= type.getNumberOfMilestones();
    }

    /******************************************
     Calculate and update milestones
     ******************************************/

    /**
     * @return true if a new milestone is reached after the update
     */
    public boolean checkReachedMilestoneUpdate(int foodCount) {
        updateMilestonesReached(foodCount);
        return currentMilestonesReached > previousMilestonesReached;
    }

    private void updateMilestonesReached(int foodCount) {
        previousMilestonesReached = currentMilestonesReached;
        currentMilestonesReached = reachedMilestones(foodCount);
    }

    /**
     * @return the number of milestones that are currently reached
     */
    private int reachedMilestones(int foodCount) {

        // TODO - Test if .length differs to .count()
        return this.type.getMilestones().stream().mapToInt(Integer::intValue)
                .filter(milestone -> foodCount >= milestone).toArray().length;
    }


}
