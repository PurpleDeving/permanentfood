package net.purple.solextended.milestonebased;

import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public class MilestoneMessage {

    public boolean anyMilestoneReached = false;
    public final List<MutableComponent> milestoneMessages = new ArrayList<>();
    public boolean anyMaxMilestoneReached = false;


}
