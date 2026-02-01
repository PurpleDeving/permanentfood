package net.purple.solextended.client.gui.pages;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.purple.solextended.client.LocalizationHelper;
import net.purple.solextended.client.gui.FoodBookScreen;
import net.purple.solextended.client.gui.elements.UIElement;

import java.awt.*;

/**
 * First page of the Food Book showing basic statistics.
 * Package-accessible for use by FoodBookScreen.
 */
@OnlyIn(Dist.CLIENT)
public final class StatsPage extends Page {
    public StatsPage(int foodsEaten, Rectangle frame) {
        super(frame, LocalizationHelper.localizedComponent("gui", "food_book.stats.title"));

        // Create a simple display showing number of foods eaten
        UIElement foodsEatenStat = statWithIcon(
                FoodBookScreen.CARROT_ICON,
                String.valueOf(foodsEaten),
                LocalizationHelper.localizedComponent("gui", "food_book.stats.foods_eaten").getString()
        );
        mainStack.addChild(foodsEatenStat);

        mainStack.addChild(makeSeparatorLine());

        updateMainStack();
    }
}
