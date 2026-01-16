package net.purple.permfood.moddata;

import io.netty.buffer.ByteBuf;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.purple.permfood.Constants;
import net.purple.permfood.config.Config;
import org.jline.utils.Log;

import java.util.List;

import static net.purple.permfood.config.Config.NATURAL_REGEN_THRESHOLD_WITH_SATURATION;
import static net.purple.permfood.config.Config.NATURAL_REGEN_THRESHOLD_NO_SATURATION;

public class PlayerValues {

    private int foodCount;

    private int max_hunger;
    private int natural_regen_threshold_with_saturation;
    private int natural_regen_threshold_no_saturation;
    private float max_saturation;
    private float max_exhaustion;


    public PlayerValues(int foodCount) {

        // SHould only be called on server side. Client side should get values from server via network packet
        if (EffectiveSide.get().isServer()) {
            updateValues(foodCount);
        }


    }


    // Full constructor used when decoding from the network so client gets exact same values
    public PlayerValues(int foodCount, int max_hunger, int natural_regen_threshold_with_saturation, int natural_regen_threshold_no_saturation, float max_saturation, float max_exhaustion) {
        if (EffectiveSide.get().isServer()) {
            Log.warn("PlayerValues full constructor called on the server side! This should only be used on the client side when reading from the network.");
        }
        this.foodCount = foodCount;
        this.max_hunger = max_hunger;
        this.natural_regen_threshold_with_saturation = natural_regen_threshold_with_saturation;
        this.natural_regen_threshold_no_saturation = natural_regen_threshold_no_saturation;
        this.max_saturation = max_saturation;
        this.max_exhaustion = max_exhaustion;
    }

    // Helper methods to calculate values based on milestones
    public static int getValueInt(int defaultValue, int valuePerMilestone, List<? extends Integer> milestones, int foodCount) {
        int reachedMilestones = 0;

        for (int milestone : milestones) {
            if (foodCount >= milestone) {
                reachedMilestones++;
            } else {
                break;
            }
        }

        return defaultValue + (valuePerMilestone * reachedMilestones);
    }

    public static float getValueFloat(float defaultValue, float valuePerMilestone, List<? extends Integer> milestones, int foodCount) {
        int reachedMilestones = 0;

        for (int milestone : milestones) {
            if (foodCount >= milestone) {
                reachedMilestones++;
            } else {
                break;
            }
        }

        return defaultValue + (valuePerMilestone * reachedMilestones);
    }

    public static double getAttributeValue(double valuePerMilestone, List<? extends Integer> milestones, int foodCount) {
        int reachedMilestones = 0;

        for (int milestone : milestones) {
            if (foodCount >= milestone) {
                reachedMilestones++;
            } else {
                break;
            }
        }

        return (valuePerMilestone * reachedMilestones);
    }

    // Read all fields from the ByteBuf and construct a PlayerValues instance with exact values
    public static PlayerValues readFromBuf(ByteBuf buf) {
        int foodCount = buf.readInt();
        int max_hunger = buf.readInt();
        int nat_with = buf.readInt();
        int nat_no = buf.readInt();
        float max_sat = buf.readFloat();
        float max_ex = buf.readFloat();

        return new PlayerValues(foodCount, max_hunger, nat_with, nat_no, max_sat, max_ex);
    }

    protected void updateValues(int foodCount) {
        this.foodCount = foodCount;

        //Hunger
        if (Config.ENABLE_MAX_HUNGER_CHANGES.getAsBoolean()) {
            this.max_hunger = getValueInt(Config.NEW_BASE_MAX_HUNGER.getAsInt(), Config.MAX_HUNGER_PER_MILESTONE.getAsInt(), Config.MILESTONES_FOR_MAX_HUNGER.get(), foodCount);
        } else {
            this.max_hunger = Constants.VANILLA_MAX_HUNGER;
        }
        // Natural Regen
        this.natural_regen_threshold_with_saturation = (int) (((long) NATURAL_REGEN_THRESHOLD_WITH_SATURATION.getAsInt() * this.max_hunger) / 100);
        this.natural_regen_threshold_no_saturation = (int) (((long) NATURAL_REGEN_THRESHOLD_NO_SATURATION.getAsInt() * this.max_hunger) / 100);
        //Saturation
        if (Config.ENABLE_MAX_SATURATION_CHANGES.getAsBoolean()) {
            this.max_saturation = getValueFloat((float) Config.NEW_BASE_MAX_SATURATION.getAsDouble(), (float) Config.MAX_SATURATION_PER_MILESTONE.getAsDouble(), Config.MILESTONES_FOR_MAX_SATURATION.get(), foodCount);
        } else {
            this.max_saturation = Constants.VANILLA_MAX_SATURATION;
        }
        //Exhaustion
        if (Config.ENABLE_EXHAUSTION_CHANGES.getAsBoolean()) {
            this.max_exhaustion = getValueFloat((float) Config.NEW_BASE_MAX_EXHAUSTION.getAsDouble(), (float) Config.MAX_EXHAUSTION_PER_MILESTONE.getAsDouble(), Config.MILESTONES_FOR_MAX_EXHAUSTION.get(), foodCount);
        } else {
            this.max_exhaustion = Constants.VANILLA_MAX_EXHAUSTION;
        }

    }

    // Getters for all fields
    public int getMax_hunger() {
        return max_hunger;
    }

    public int getNatural_regen_threshold_with_saturation() {
        return natural_regen_threshold_with_saturation;
    }

    public int getNatural_regen_threshold_no_saturation() {
        return natural_regen_threshold_no_saturation;
    }

    public float getMax_saturation() {
        return max_saturation;
    }

    public float getMax_exhaustion() {
        return max_exhaustion;
    }

    public int getFoodCount() {
        return foodCount;
    }

    /******************************************
     Network and Client shit
     ******************************************/


    // Write all fields to the ByteBuf so the client receives exact values from the server
    public void writeToBuf(ByteBuf buf) {
        buf.writeInt(this.foodCount);
        buf.writeInt(this.max_hunger);
        buf.writeInt(this.natural_regen_threshold_with_saturation);
        buf.writeInt(this.natural_regen_threshold_no_saturation);
        buf.writeFloat(this.max_saturation);
        buf.writeFloat(this.max_exhaustion);
    }
}
