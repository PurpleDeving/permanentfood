package net.purple.permfood.moddata;

import io.netty.buffer.ByteBuf;
import net.purple.permfood.Constants;
import net.purple.permfood.config.Config;

import java.util.List;

import static net.purple.permfood.config.Config.NATURAL_REGEN_THRESHOLD_NO_SATURATION;
import static net.purple.permfood.config.Config.NATURAL_REGEN_THRESHOLD_WITH_SATURATION;

public class PlayerValues {

    // Stats that are external but not values
    private int foodCount;
    private int natural_regen_threshold_with_saturation;
    private int natural_regen_threshold_no_saturation;

    // Player Values
    private PlayerValue max_hunger;
    private float max_saturation;
    private float max_exhaustion;


    // Used from booth sides. Config is synced, so result should be same.
    public PlayerValues(int foodCount) {

        this.max_hunger = new PlayerValue();
        updateValues(foodCount);

    }

    public void clearList() {
        this.updateValues(0);
    }

    public static class PlayerValue {

        private Number value;
        private List<? extends Integer> milestones;

        public void updateValues(Number value) {
            this.value = value;
        }

        public void updateMilestone(List<? extends Integer> milestones) {
            this.milestones = milestones;
        }

        public int getInt() {
            return value.intValue();
        }

        public List<? extends Integer> getMilestones() {
            return milestones;
        }
    }


    public static int getMilestonesReached(List<? extends Integer> milestones, int foodCount) {
        int reachedMilestones = 0;

        for (int milestone : milestones) {
            if (foodCount >= milestone) {
                reachedMilestones++;
            } else {
                break;
            }
        }

        return reachedMilestones;
    }


    protected void updateValues(int foodCount) {

        List<? extends Integer> milestones;

        //Hunger
        milestones = Config.MILESTONES_FOR_MAX_HUNGER.get();
        this.max_hunger.updateMilestone(milestones);

        if (Config.ENABLE_MAX_HUNGER_CHANGES.getAsBoolean()) {
            int milestonesReached = getMilestonesReached(milestones, foodCount);
            this.max_hunger.updateValues((Config.NEW_BASE_MAX_HUNGER.getAsInt() + (Config.MAX_HUNGER_PER_MILESTONE.getAsInt() * milestonesReached)));
        } else {
            this.max_hunger.updateValues(Constants.VANILLA_MAX_HUNGER);
        }

        //Saturation
        if (Config.ENABLE_MAX_SATURATION_CHANGES.getAsBoolean()) {
            int milestonesReached = getMilestonesReached(Config.MILESTONES_FOR_MAX_SATURATION.get(), foodCount);

            this.max_saturation = ((float) Config.NEW_BASE_MAX_SATURATION.getAsDouble() + (float) Config.MAX_SATURATION_PER_MILESTONE.getAsDouble() * milestonesReached);
        } else {
            this.max_saturation = (Constants.VANILLA_MAX_SATURATION);
        }
        //Exhaustion
        if (Config.ENABLE_EXHAUSTION_CHANGES.getAsBoolean()) {
            int milestonesReached = getMilestonesReached(Config.MILESTONES_FOR_MAX_EXHAUSTION.get(), foodCount);

            this.max_exhaustion = ((float) Config.NEW_BASE_MAX_EXHAUSTION.getAsDouble() + (float) Config.MAX_EXHAUSTION_PER_MILESTONE.getAsDouble() * milestonesReached);
        } else {
            this.max_exhaustion = (Constants.VANILLA_MAX_EXHAUSTION);
        }

        // Natural Regen
        this.natural_regen_threshold_with_saturation = ((int) (((long) NATURAL_REGEN_THRESHOLD_WITH_SATURATION.getAsInt() * this.max_hunger.getInt()) / 100));
        this.natural_regen_threshold_no_saturation = ((int) (((long) NATURAL_REGEN_THRESHOLD_NO_SATURATION.getAsInt() * this.max_hunger.getInt()) / 100));

        this.foodCount = foodCount;

    }

    // Getters for all fields
    public int getMax_hunger() {
        return this.max_hunger.getInt();
    }

    public int getNatural_regen_threshold_with_saturation() {
        return this.natural_regen_threshold_with_saturation;
    }

    public int getNatural_regen_threshold_no_saturation() {
        return this.natural_regen_threshold_no_saturation;
    }

    public float getMax_saturation() {
        return this.max_saturation;
    }

    public float getMax_exhaustion() {
        //Log.warn("Max exhaustion on " + EffectiveSide.get() + " is: " + this.max_exhaustion);
        return this.max_exhaustion;
    }

    public int getFoodCount() {
        return foodCount;
    }


    /******************************************
     Network and Client shit
     ******************************************/


    // Read all fields from the ByteBuf and construct a PlayerValues instance with exact values
    public static PlayerValues readFromBuf(ByteBuf buf) {
        int foodCount = buf.readInt();

        return new PlayerValues(foodCount);
    }

    // Write all fields to the ByteBuf so the client receives exact values from the server
    public void writeToBuf(ByteBuf buf) {
        buf.writeInt(this.foodCount);
    }


    // TODO desolve this

    public static double getAttributeValue(double valuePerMilestone, List<? extends Integer> milestones, int foodCount) {

        return (valuePerMilestone * getMilestonesReached(milestones, foodCount));
    }
}
