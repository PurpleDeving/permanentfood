package net.purple.permfood.moddata.baseClases;

/**
 * Represents that a {@link MilestoneBased} advanced milestones between two snapshots.
 *
 * @param name       display/key name of the milestone-based stat
 * @param oldReached reached milestones before update
 * @param newReached reached milestones after update
 */
public record MilestoneDiff(String name, int oldReached, int newReached) {

    public int delta() {
        return Math.max(0, newReached - oldReached);
    }

    public boolean advanced() {
        return newReached > oldReached;
    }
}
