package net.purple.permfood.moddata.baseClases;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public abstract class PlayerFoodInstance {

    private int foodCount = 0;
    private final Player player;

    private final List<MilestoneBased> MilestoneBasedList = new ArrayList<>();

    public PlayerFoodInstance(Player player) {
        this.player = player;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
        updateBuffs(foodCount);
    }

    protected List<? extends MilestoneBased> getRawMilestoneBasedList() {
        return MilestoneBasedList;
    }


    /******************************************
     Enforce Behaivor
     ******************************************/

    protected void addMilestoneBased(MilestoneBased milestoneBased) {
        this.MilestoneBasedList.add(milestoneBased);
    }

    protected abstract void updateMilestones();

    public abstract List<? extends MilestoneBased> getMilestoneBasedList();

    protected void updateBuffs() {
        updateBuffs(this.getFoodCount());
    }

    public abstract void updateBuffs(int foodCount);


    /******************************************
     Networking
     ******************************************/

    // Write all fields to the ByteBuf so the client receives exact values from the server
    public void writeToBuf(ByteBuf buf) {
        buf.writeInt(this.foodCount);
    }

    public static PlayerFoodInstance readFromBuf(ByteBuf buf, Class<? extends PlayerFoodInstance> clazz, Player player) {

        int foodCount = buf.readInt();

        try {
            Constructor<? extends PlayerFoodInstance> constructor = clazz.getConstructor(Integer.class);
            return constructor.newInstance(player, foodCount);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance of " + clazz.getSimpleName(), e);
        }


    }

    public Player getPlayer() {
        return this.player;
    }
}
