package net.purple.permfood.moddata.attributes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.purple.permfood.moddata.baseClases.MilestoneBased;

public class PlayerAttribute extends MilestoneBased {

    private final Holder<Attribute> attribute;
    private double valuePerMilestone;


    public PlayerAttribute(Holder<Attribute> attribute) {
        super(attribute.getRegisteredName());
        this.attribute = attribute;
    }

    public void setValuePerMilestone(double valuePerMilestone) {
        this.valuePerMilestone = valuePerMilestone;
    }


    public double getAddedValue() {
        return this.getMilestonesReached() * valuePerMilestone;
    }


}
