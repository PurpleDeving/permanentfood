package net.purple.solextended.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.purple.solextended.config.Configs;
import net.purple.solextended.foodlist.FoodList;
import net.purple.solextended.foodlist.PlayerFoodList;

import static net.purple.solextended.SolExtended.FOOD_LIST_ATTACHMENT;
import static net.purple.solextended.SolExtended.MODID;
import static net.purple.solextended.foodlist.FoodList.isFood;
import static net.purple.solextended.foodlist.FoodList.isHealthy;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = MODID)
public class TooltipHandler {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (!Configs.solClientConfig.isFoodTooltipEnabled) {
            return;
        }

        Player player = event.getEntity();
        if (player == null) {
            return;
        }

        Item item = event.getItemStack().getItem();
        var tooltip = event.getToolTip();

        // Only proceed if the item is food
        if (!isFood(item)) {
            return;
        }

        boolean isAllowed = FoodList.isFoodAllowed(item);
        if (!isAllowed) { // Item is food but not allowed
            if (!isHealthy(item)) {
                tooltip.add(localizedTooltip("disabled.cheap_food", ChatFormatting.DARK_GRAY));
            } else { // If food is healthy but not allowed, it must be the whitelist/blacklist
                tooltip.add(localizedTooltip("disabled.white_black_list", ChatFormatting.GRAY));
            }
        }
        PlayerFoodList playerFoodList = player.getData(FOOD_LIST_ATTACHMENT);
        boolean hasBeenEaten = playerFoodList.hasEaten(item);


        if (hasBeenEaten) {
            tooltip.add(localizedTooltip("disabled.eaten", ChatFormatting.GREEN));
        } else {
            tooltip.add(localizedTooltip("enabled.not_eaten", ChatFormatting.DARK_GRAY));
        }

    }

    private static MutableComponent localizedTooltip(String path, ChatFormatting color) {
        return localizedComponent("tooltip", path).withStyle(color);
    }

    public static MutableComponent localizedComponent(String domain, String path, Object... args) {
        return Component.translatable(keyString(domain, path), args);
    }

    public static String keyString(String domain, String path) {
        return domain + "." + MODID + "." + path;
    }
}
