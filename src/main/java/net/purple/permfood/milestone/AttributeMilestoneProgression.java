package net.purple.permfood.milestone;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.purple.solextended.api.milestonebased.MilestoneProgression;
import net.purple.solextended.client.LocalizationHelper;

import java.util.Objects;

public class AttributeMilestoneProgression extends MilestoneProgression {

    private final double valuePerMilestone;

    public AttributeMilestoneProgression(AttributeMilestoneType type, double valuePerMilestone) {
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

    @Override // TODO This is the wrong Localization Helper
    public MutableComponent getCelebrationMessage(Player player) {

        AttributeMilestoneType attributeMilestoneType = (AttributeMilestoneType) getType();
        double attributevalue = Objects.requireNonNull(player.getAttribute(attributeMilestoneType.getAttribute())).getValue(); // TODO - Need to limit to 1 decimal place ?

        //TODO Type must be solvable better than this
        return LocalizationHelper.localizedComponent("celebration", "celebration.message", attributeMilestoneType.getDeclareName(), attributevalue, getBuffValue());
    }
}

