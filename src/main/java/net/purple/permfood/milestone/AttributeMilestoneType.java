package net.purple.permfood.milestone;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.purple.solextended.api.milestonebased.MilestoneType;

import java.util.List;

import static net.purple.permfood.PermanentFood.MODID;

public class AttributeMilestoneType extends MilestoneType {

    Holder<Attribute> attribute;

    public AttributeMilestoneType(String name, List<? extends Integer> milestones, Holder<Attribute> attribute) {
        super(name, milestones);
        this.attribute = attribute;
    }

    public Holder<Attribute> getAttribute() {
        return attribute;
    }

    public ResourceLocation getResourceLocation() {
        return ResourceLocation.fromNamespaceAndPath(MODID, this.getName());
    }

}
