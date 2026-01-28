package backup.moddata.baseClases;

import net.minecraft.world.entity.player.Player;

import javax.annotation.CheckForNull;
import java.util.HashMap;
import java.util.UUID;

public class PlayerFoodInstances {

    private static final HashMap<UUID, PlayerFoodInstances> playerFoodInstances = new HashMap<>();

    public static void registerPlayerFoodInstance(UUID uuid, PlayerFoodInstances playerFoodInstancesInstance) {
        playerFoodInstances.put(uuid, playerFoodInstancesInstance);
    }

    @CheckForNull
    public static PlayerFoodInstances getUUIDFoodInstance(UUID uuid) {
        return playerFoodInstances.get(uuid);
    }

    @CheckForNull
    public static PlayerFoodInstances getUUIDFoodInstance(Player player) {
        return playerFoodInstances.get(player.getUUID());
    }


}
