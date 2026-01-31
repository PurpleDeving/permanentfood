package net.purple.solextended.api.milestonebased;

import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Base class for all milestone-based progression managers.
 * Each player gets their own instance via DataAttachment.
 *
 * <p>Implementations should:
 * <ul>
 *   <li>Update each {@link MilestoneProgression} using {@link MilestoneProgression#checkReachedMilestoneUpdate(int)}.</li>
 *   <li>Apply their side-effects/modifiers to the provided {@link Player}.</li>
 * </ul>
 *
 * @param <T> The type of milestone progression this manager handles
 */
public abstract class MilestoneManager<T extends MilestoneProgression> {

    private final Set<T> milestoneProgressions = new HashSet<>();

    public Set<T> getMilestoneProgressions() {
        return milestoneProgressions;
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
}
