package net.purple.solextended.client.gui.pages;

import net.purple.solextended.client.gui.FoodBookScreen;
import net.purple.solextended.client.gui.elements.UIElement;

import java.awt.*;

/**
 * First page of the Food Book showing basic statistics.
 * Package-accessible for use by FoodBookScreen.
 */
public final class StatsPage extends Page {
    public StatsPage(int foodsEaten, Rectangle frame) {
        super(frame, "Food Statistics");

        // Create a simple display showing number of foods eaten
        UIElement foodsEatenStat = statWithIcon(
                FoodBookScreen.CARROT_ICON,
                String.valueOf(foodsEaten),
                "Foods Eaten"
        );
        mainStack.addChild(foodsEatenStat);

        mainStack.addChild(makeSeparatorLine());

        updateMainStack();
    }
}
