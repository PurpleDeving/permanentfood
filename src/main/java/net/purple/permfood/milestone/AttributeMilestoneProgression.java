package net.purple.permfood.milestone;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.purple.permfood.PermanentFood;
import net.purple.solextended.client.LocalizationHelper;
import net.purple.solextended.milestonebased.MilestoneProgression;

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

    @Override
    public MutableComponent getCelebrationMessage(Player player) {

        AttributeMilestoneType attributeMilestoneType = (AttributeMilestoneType) getType();

        AttributeInstance instance = player.getAttribute(attributeMilestoneType.getAttribute());
        double attributeValue = instance != null ? instance.getValue() : 0.0;

        // Display values with a consistent single decimal place.
        double shownAttributeValue = round(attributeValue, 1);
        double shownBuffValue = round(getBuffValue(), 1);

        if (this.maxMilestonesReached()) {
            // domain.modid.path => celebration.permanentfood.celebration.max_message
            return LocalizationHelper.localizedComponent(PermanentFood.MODID, "celebration", "celebration.max_message",
                    attributeMilestoneType.getDeclareName(), shownAttributeValue, shownBuffValue).withStyle(ChatFormatting.GOLD);

        } else {
            // domain.modid.path => celebration.permanentfood.celebration.message
            return LocalizationHelper.localizedComponent(PermanentFood.MODID, "celebration", "celebration.message",
                    attributeMilestoneType.getDeclareName(), shownAttributeValue, shownBuffValue);
        }
    }
}
