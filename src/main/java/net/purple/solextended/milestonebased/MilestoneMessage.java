package net.purple.solextended.milestonebased;

import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public record MilestoneMessage(boolean anyMilestoneReached, List<MutableComponent> milestoneMessages,
                               boolean isMaxMilestoneReached) {

    public MilestoneMessage() {
        this(false, new ArrayList<MutableComponent>(), false);
    }


}
