package net.purple.solextended.client.gui.examples;

import net.minecraft.world.entity.player.Player;
import net.purple.solextended.client.gui.FoodBookScreen;
import net.purple.solextended.client.gui.PageRegistry;
import net.purple.solextended.client.gui.elements.UIElement;
import net.purple.solextended.client.gui.elements.UILabel;
import net.purple.solextended.client.gui.elements.UIStack;
import net.purple.solextended.client.gui.pages.Page;

import java.awt.*;

/**
 * Example demonstrating how external mods can add custom pages to the Food Book.
 * <p>
 * To use this in your mod:
 * 1. Copy this class to your mod
 * 2. Call registerExamplePage() during your mod initialization
 * 3. The page will appear in the Food Book between stats and eaten foods
 */
public class ExternalPageExample {

    /**
     * Call this from your mod's constructor or initialization event
     */
    public static void registerExamplePage() {
        PageRegistry.registerExternalPage("examplemod", (player, frame) -> {
            return new ExampleCustomPage(player, frame);
        });
    }

    /**
     * Example custom page showing various UI elements
     */
    private static class ExampleCustomPage extends Page {

        public ExampleCustomPage(Player player, Rectangle frame) {
            super(frame, "Example Custom Page");

            // Example 1: Simple text label
            UILabel welcomeLabel = new UILabel("Welcome to my custom page!");
            welcomeLabel.color = FULL_BLACK;
            mainStack.addChild(welcomeLabel);

            mainStack.addChild(makeSeparatorLine());

            // Example 2: Stat with icon
            UIElement playerLevelStat = statWithIcon(
                    FoodBookScreen.CARROT_ICON,  // You can use built-in icons
                    String.valueOf(player.experienceLevel),
                    "Player Level"
            );
            mainStack.addChild(playerLevelStat);

            mainStack.addChild(makeSeparatorLine());

            // Example 3: Custom fraction display
            int current = 10;
            int total = 20;
            UIElement progressStat = statWithIcon(
                    FoodBookScreen.CARROT_ICON,
                    fraction(current, total),
                    "My Custom Progress"
            );
            mainStack.addChild(progressStat);

            mainStack.addChild(makeSeparatorLine());

            // Example 4: Custom horizontal stack
            UIStack horizontalInfo = new UIStack();
            horizontalInfo.axis = UIStack.Axis.HORIZONTAL;
            horizontalInfo.spacing = 10;

            UILabel label1 = new UILabel("Info A");
            label1.color = LESS_BLACK;
            horizontalInfo.addChild(label1);

            UILabel label2 = new UILabel("Info B");
            label2.color = LESS_BLACK;
            horizontalInfo.addChild(label2);

            mainStack.addChild(horizontalInfo);

            // Important: Always call this after adding all elements
            updateMainStack();
        }
    }

    /**
     * Example showing how to create a page with your own custom icons
     */
    private static class CustomIconExample extends Page {

        public CustomIconExample(Player player, Rectangle frame) {
            super(frame, "Custom Icons Example");

            // Define your own icon from your mod's texture
            // ImageData customIcon = new ImageData(
            //     ResourceLocation.fromNamespaceAndPath("yourmod", "textures/gui/icons.png"),
            //     new Rectangle(0, 0, 16, 16),  // position in texture
            //     14, 14  // visual size
            // );

            // Use your custom icon in stats
            // UIElement stat = statWithIcon(
            //     customIcon,
            //     "123",
            //     "Your Custom Stat"
            // );
            // mainStack.addChild(stat);

            UILabel infoLabel = new UILabel("This is a template for custom icons");
            infoLabel.color = LESS_BLACK;
            mainStack.addChild(infoLabel);

            updateMainStack();
        }
    }
}
