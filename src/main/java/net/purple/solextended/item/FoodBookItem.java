package net.purple.solextended.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import net.purple.solextended.SolExtended;
import net.purple.solextended.client.gui.FoodBookScreen;

import static net.purple.solextended.SolExtended.FOOD_LIST_ATTACHMENT;
import static net.purple.solextended.SolExtended.IS_DEV;

public final class FoodBookItem extends Item {
    public FoodBookItem() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (player.isLocalPlayer()) {
            if (FMLEnvironment.dist.isClient()) {
                if (IS_DEV) {
                    SolExtended.LOGGER.warn("Loading Food Book Screen for player with eaten foods: " +
                            player.getData(FOOD_LIST_ATTACHMENT).getEatenFoods().toString());
                }
                FoodBookScreen.open(player);
            }
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }
}
