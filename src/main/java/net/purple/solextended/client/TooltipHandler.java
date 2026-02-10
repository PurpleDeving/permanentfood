package net.purple.solextended.client;

import net.minecraft.ChatFormatting;
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
                tooltip.add(LocalizationHelper.localizedTooltip(MODID, "disabled.cheap_food", ChatFormatting.DARK_GRAY));
            } else { // If food is healthy but not allowed, it must be the whitelist/blacklist
                tooltip.add(LocalizationHelper.localizedTooltip(MODID, "disabled.white_black_list", ChatFormatting.GRAY));
            }
            return;
        }
        PlayerFoodList playerFoodList = player.getData(FOOD_LIST_ATTACHMENT);
        boolean hasBeenEaten = playerFoodList.hasEaten(item);


        if (hasBeenEaten) {
            tooltip.add(LocalizationHelper.localizedTooltip(MODID, "enabled.eaten", ChatFormatting.GREEN));
        } else {
            tooltip.add(LocalizationHelper.localizedTooltip(MODID, "enabled.not_eaten", ChatFormatting.DARK_AQUA));
        }

    }

}
