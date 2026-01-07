package net.purple.permfood;

import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.purple.permfood.mixin.FoodDataMixin;
import net.purple.permfood.mixin.PlayerMixin;

import java.util.HashMap;
import java.util.UUID;

import static net.purple.permfood.PermanentFood.MODID;

@EventBusSubscriber(modid = MODID)
public final class PlayerValueHandler {

    public static final HashMap<UUID, PlayerValues> PLAYER_VALUES = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        // Populate HashMap with a new PlayerValue and instantly update it.
        Player player = event.getEntity();
        PLAYER_VALUES.put(player.getUUID(), new PlayerValues(player, FoodList.get(player).getEatenFoodCount()));
    }

    //TODO ReloadConfig needs to update all PlayerValues


}
