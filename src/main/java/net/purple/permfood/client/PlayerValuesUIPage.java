package net.purple.permfood.client;

import com.cazsius.solcarrot.client.gui.elements.UIElement;
import com.cazsius.solcarrot.client.gui.elements.UILabel;
import com.cazsius.solcarrot.client.gui.elements.UIStack;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.Rectangle;

/**
 * Minimal page-like UIElement that can be inserted into the FoodBookScreen.pages list.
 * This avoids extending the package-private Page class and doesn't create com.cazsius
 * sources inside this project.
 */
public final class PlayerValuesUIPage extends UIElement {

    public PlayerValuesUIPage(Rectangle frame) {
        super(frame);

        UIStack mainStack = new UIStack();
        mainStack.axis = UIStack.Axis.VERTICAL;
        mainStack.spacing = 6;

        // simple header
        UILabel header = new UILabel("Player Values");
        mainStack.addChild(header);

        // placeholder content
        UILabel placeholder = new UILabel("(PermanentFood) No player values implemented yet.");
        mainStack.addChild(placeholder);

        // align and add
        mainStack.setCenterX(getCenterX());
        mainStack.setCenterY(getCenterY());

        children.add(mainStack);
    }

    @Override
    protected void render(GuiGraphics graphics) {
        super.render(graphics);
    }
}
