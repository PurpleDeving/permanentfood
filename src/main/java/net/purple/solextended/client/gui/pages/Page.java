package net.purple.solextended.client.gui.pages;

import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.purple.solextended.client.gui.elements.*;

import java.awt.*;

/**
 * Base class for all Food Book pages.
 * Provides helper methods for creating common page elements.
 * External mods can extend this to create custom pages.
 */
@OnlyIn(Dist.CLIENT)
public abstract class Page extends UIElement {
    protected final UIStack mainStack;
    protected final int spacing = 6;

    // Common colors used across pages
    protected static final Color FULL_BLACK = Color.BLACK;
    protected static final Color LESS_BLACK = new Color(0, 0, 0, 128);
    protected static final Color LEAST_BLACK = new Color(0, 0, 0, 64);

    protected Page(Rectangle frame, String header) {
        this(frame, Component.literal(header));
    }

    protected Page(Rectangle frame, Component header) {
        super(frame);

        mainStack = new UIStack();
        mainStack.axis = UIStack.Axis.VERTICAL;
        mainStack.spacing = spacing;

        UILabel headerLabel = new UILabel(header);
        mainStack.addChild(headerLabel);

        mainStack.addChild(makeSeparatorLine());

        children.add(mainStack);
        updateMainStack();
    }

    protected void updateMainStack() {
        mainStack.setCenterX(getCenterX());
        mainStack.setMinY(getMinY() + 17);
        mainStack.updateFrames();
    }

    /**
     * Helper to format a fraction string (e.g., "5/10")
     */
    protected String fraction(int numerator, int denominator) {
        return numerator + "/" + denominator;
    }

    /**
     * Creates a horizontal separator line
     */
    protected UIElement makeSeparatorLine() {
        return UIBox.horizontalLine(0, getWidth() / 2, 0, LEAST_BLACK);
    }

    /**
     * Creates a stat display with an icon, value, and name
     */
    protected UIElement statWithIcon(ImageData icon, String value, String name) {
        UIStack valueStack = new UIStack();
        valueStack.axis = UIStack.Axis.HORIZONTAL;
        valueStack.spacing = 3;

        valueStack.addChild(new UIImage(icon));
        valueStack.addChild(new UILabel(value));

        UIStack fullStack = new UIStack();
        fullStack.axis = UIStack.Axis.VERTICAL;
        fullStack.spacing = 2;

        fullStack.addChild(valueStack);
        UILabel nameLabel = new UILabel(name);
        nameLabel.color = LESS_BLACK;
        fullStack.addChild(nameLabel);

        return fullStack;
    }
}
