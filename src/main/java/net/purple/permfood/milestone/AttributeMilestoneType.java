package net.purple.permfood.milestone;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.purple.solextended.milestonebased.MilestoneType;

import java.util.List;

import static net.purple.permfood.PermanentFood.MODID;

public class AttributeMilestoneType extends MilestoneType {

    Holder<Attribute> attribute;
    String declareName;

    public AttributeMilestoneType(String name, List<? extends Integer> milestones, Boolean isEnabled, Holder<Attribute> attribute, String declareName) {
        super(name, milestones, isEnabled);
        this.attribute = attribute;
        this.declareName = declareName;
    }


    public Holder<Attribute> getAttribute() {
        return attribute;
    }

    public ResourceLocation getResourceLocation() {
        return ResourceLocation.fromNamespaceAndPath(MODID, this.getName());
    }

    public String getDeclareName() {
        return declareName;
    }
}

