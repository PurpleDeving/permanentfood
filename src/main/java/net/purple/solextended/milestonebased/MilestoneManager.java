package net.purple.solextended.milestonebased;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public abstract class MilestoneManager<T extends MilestoneProgression> {

    private final List<T> milestoneProgressions = new ArrayList<>();

    /**
     * @return all milestone progressions tracked by this manager.
     */
    public List<T> getAllMilestoneProgressions() {
        return milestoneProgressions;
    }

    public List<T> getActiveMilestoneProgressions() {
        return milestoneProgressions.stream()
                .filter(progression -> progression.getType().isEnabled())
                .collect(Collectors.toList());
    }

    /**
     * Called when a player's food count changes (or needs to be re-evaluated).
     *
     * <p>This method should BOTH:
     * <ol>
     *   <li>Update all internal milestone progressions for {@code foodCount}.</li>
     *   <li>Apply the resulting effects to {@code player}.</li>
     * </ol>
     */
    public abstract void onFoodCountUpdate(Player player, int foodCount);

    /**
     * Called by the {@code /foodlist showstats} command.
     *
     * <p>Default implementation does nothing. Override to append manager-specific stats to {@code output}.
     */
    public void outputStats(ServerPlayer player, int foodCount, MutableComponent output) {
        // no-op by default
    }

    public void gatherMilestoneMessages(Player player, MilestoneMessage existingMessage) {

        for (MilestoneProgression milestoneProgression : this.getActiveMilestoneProgressions()) {
            if (milestoneProgression.checkReachedMilestoneUpdate()) {
                existingMessage.anyMilestoneReached = true;
                existingMessage.milestoneMessages.add(milestoneProgression.getCelebrationMessage(player));
                if (milestoneProgression.maxMilestonesReached()) {
                    existingMessage.anyMaxMilestoneReached = true;
                }
            }
        }

    }
}
