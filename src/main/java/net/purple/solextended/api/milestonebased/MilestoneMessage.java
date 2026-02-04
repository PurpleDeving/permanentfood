package net.purple.solextended.api.milestonebased;

import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public record MilestoneMessage(Boolean anyMilestoneReached, List<MutableComponent> milestoneMessages) {

    public MilestoneMessage() {
        this(false, new ArrayList<MutableComponent>());
    }


}
