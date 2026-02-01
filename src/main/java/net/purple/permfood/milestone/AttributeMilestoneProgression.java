package net.purple.permfood.milestone;

import net.purple.solextended.api.milestonebased.MilestoneProgression;
import net.purple.solextended.api.milestonebased.MilestoneType;

public class AttributeMilestoneProgression extends MilestoneProgression {

    private final double valuePerMilestone;

    public AttributeMilestoneProgression(MilestoneType type, double valuePerMilestone) {
        super(type);
        this.valuePerMilestone = valuePerMilestone;
    }

    public double getBuffValue() {
        return valuePerMilestone * super.getMilestonesReached();
    }

    static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

}
