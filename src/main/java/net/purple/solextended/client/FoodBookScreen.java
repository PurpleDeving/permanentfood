package net.purple.solextended.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.purple.solextended.client.gui.PageFlipButton;

@OnlyIn(Dist.CLIENT)
public class FoodBookScreen extends Screen implements PageFlipButton.Pageable {
    protected FoodBookScreen() {
        super(null);
    }

    public static void open(net.minecraft.world.entity.player.Player player) {
        Minecraft.getInstance().setScreen(new FoodBookScreen());
    }

    @Override
    public void switchToPage(int pageNumber) {
        
    }

    @Override
    public int getCurrentPageNumber() {
        return 0;
    }

    @Override
    public boolean isWithinRange(int pageNumber) {
        return false;
    }
}
