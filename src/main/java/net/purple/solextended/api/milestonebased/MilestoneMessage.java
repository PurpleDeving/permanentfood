package net.purple.solextended.api.milestonebased;

import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public class MilestoneMessage {

    private boolean anyMaxMilestoneReached;

    private final List<MutableComponent> milestoneMessages;


    public MilestoneMessage() {
        this.milestoneMessages = new ArrayList<>();
    }

    public boolean anyMaxMilestoneReached() {
        return anyMaxMilestoneReached;
    }

    public List<MutableComponent> getMilestoneMessages() {
        return milestoneMessages;
    }

}
