package net.purple.solextended.api.milestonebased;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Shared definition of a milestone progression for a given "type" of scaling.
 *
 * <p>This is intentionally simple: it stores a name and a sorted milestone list.
 * Instances of {@link MilestoneProgression} reference a type and track per-player tier state.</p>
 */
public class MilestoneType {

    private final String name;
    private volatile List<Integer> milestones;

    public MilestoneType(String name, List<? extends Integer> milestones) {
        this.name = Objects.requireNonNull(name, "name");
        setMilestones(milestones);
    }

    public String getName() {
        return name;
    }

    /**
     * Sorted, immutable list.
     */
    public List<Integer> getMilestones() {
        return milestones;
    }

    public void updateMilestones(List<? extends Integer> milestones) {
        setMilestones(milestones);
    }


    private void setMilestones(List<? extends Integer> input) {
        Objects.requireNonNull(input, "milestones");

        Collections.sort(input);
        this.milestones = Collections.unmodifiableList(input);

        // TODO - Check is this works correctly
    }

    public int getNumberOfMilestones() {
        return milestones.size();
    }

}
