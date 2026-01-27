package backup.moddata.attributes;

import backup.moddata.baseClases.MilestoneBased;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;

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
