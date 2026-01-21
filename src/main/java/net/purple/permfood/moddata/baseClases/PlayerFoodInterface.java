package net.purple.permfood.moddata.baseClases;

import java.util.List;

public interface PlayerFoodInterface {

    void updateBuffs();

    void updateBuffs(int foodCount);

    void clearList();

    List<? extends MilestoneBased> getMilestoneBasedList();

}
