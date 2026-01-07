package net.purple.permfood;

import net.minecraft.world.entity.player.Player;
import org.jline.utils.Log;

import java.util.List;

import static net.purple.permfood.Config.NATURAL_REGEN_THRESHOLD_WITH_SATURATION;
import static net.purple.permfood.Config.NATURAL_REGEN_THRESHOLD_NO_SATURATION;

public class PlayerValues {

    private final Player player;
    private int foodCount;

    private int max_hunger;
    private int natural_regen_threshold_with_saturation;
    private int natural_regen_threshold_no_saturation;
    private float max_saturation;


    private float max_exhaustion;

    private int armor;


    public PlayerValues(Player player, int foodCount) {

        //TODO > Set all Attributes by foodCount depending on 
        this.player = player;

        updateValues(foodCount);
    }

    public void updateValues(int foodCount) {

        Log.info("The food Values are Updated - Code 3321");

        this.foodCount = foodCount;

        if (Config.ENABLE_MAX_HUNGER_CHANGES.getAsBoolean()) {
            this.max_hunger = getValueInt(Config.NEW_BASE_MAX_HUNGER.getAsInt(), Config.MAX_HUNGER_PER_MILESTONE.getAsInt(), Config.MILESTONES_FOR_MAX_HUNGER.get(), foodCount);
        } else {
            this.max_hunger = Constants.VANILLA_MAX_HUNGER;
        }
        this.natural_regen_threshold_with_saturation = (int) (((long) NATURAL_REGEN_THRESHOLD_WITH_SATURATION.getAsInt() * this.max_hunger) / 100);
        this.natural_regen_threshold_no_saturation = (int) (((long) NATURAL_REGEN_THRESHOLD_NO_SATURATION.getAsInt() * this.max_hunger) / 100);

        if (Config.ENABLE_MAX_SATURATION_CHANGES.getAsBoolean()) {
            this.max_saturation = getValueFloat((float) Config.NEW_BASE_MAX_SATURATION.getAsDouble(), (float) Config.MAX_SATURATION_PER_MILESTONE.getAsDouble(), Config.MILESTONES_FOR_MAX_SATURATION.get(), foodCount);
        } else {
            this.max_saturation = Constants.VANILLA_MAX_SATURATION;
        }

        if (Config.ENABLE_EXHAUSTION_CHANGES.getAsBoolean()) {
            this.max_exhaustion = getValueFloat((float) Config.NEW_BASE_MAX_EXHAUSTION.getAsDouble(), (float) Config.MAX_EXHAUSTION_PER_MILESTONE.getAsDouble(), Config.MILESTONES_FOR_MAX_EXHAUSTION.get(), foodCount);
        } else {
            this.max_exhaustion = Constants.VANILLA_MAX_EXHAUSTION;
        }

        //this.armour = armour;
    }


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


    public Player getPlayer() {
        return player;
    }

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
}
