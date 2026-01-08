package net.purple.permfood.moddata;

import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.purple.permfood.Networking.packet.PlayerValuesData;
import org.jline.utils.Log;

import static net.purple.permfood.PermanentFood.MODID;
import static net.purple.permfood.moddata.ModData.PLAYER_VALUES;

@EventBusSubscriber(modid = MODID)
public final class PlayerValueHandler {


    // Is Server side online
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        Player player = event.getEntity();


        player.getData(PLAYER_VALUES).updateValues(//30); // For Testing
                FoodList.get(player).getEatenFoodCount());

        PacketDistributor.sendToPlayer((ServerPlayer) player, new PlayerValuesData(player.getData(ModData.PLAYER_VALUES)));
    }


    // Is Server side online
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {

    }


    //TODO ReloadConfig needs to update all PlayerValues return on Client side


    // Add all the cases a player fucking changes and change the player in its foodData
}
