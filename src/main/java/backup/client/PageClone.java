/*
package backup.client;

import com.cazsius.solcarrot.client.gui.elements.*;

import java.awt.*;

import static com.cazsius.solcarrot.lib.Localization.localized;

public class PageClone extends UIElement {

    // Because i dont have access to FoodBookScreen
    public static final Color lessBlack = new Color(0, 0, 0, 128);
    public static final Color leastBlack = new Color(0, 0, 0, 64);

    final UIStack mainStack;
    final int spacing = 6;

    public PageClone(Rectangle frame, String header) {
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

    void updateMainStack() {
        mainStack.setCenterX(getCenterX());
        mainStack.setMinY(getMinY() + 17);
        mainStack.updateFrames();
    }

    String fraction(int numerator, int denominator) {
        return localized("gui", "food_book.fraction",
                numerator,
                denominator
        );
    }

    UIElement makeSeparatorLine() {
        return UIBox.horizontalLine(0, getWidth() / 2, 0, leastBlack);
    }

    UIElement statTextWithIcon(ImageData icon, String value, String name) {
        UIStack valueStack = new UIStack();
        valueStack.axis = UIStack.Axis.HORIZONTAL;
        valueStack.spacing = 3;

        valueStack.addChild(new UIImage(icon));
        valueStack.addChild(new UILabel(value));

        UIStack fullStack = new UIStack();
        fullStack.axis = UIStack.Axis.VERTICAL;
        fullStack.spacing = 4; //Was 2

        fullStack.addChild(valueStack);
        UILabel nameLabel = new UILabel(name);
        nameLabel.color = lessBlack;
        fullStack.addChild(nameLabel);

        return fullStack;
    }

    UIElement statWithIcon(ImageData icon, String value, String name) {
        UIStack valueStack = new UIStack();
        valueStack.axis = UIStack.Axis.HORIZONTAL;
        valueStack.spacing = 3;

        valueStack.addChild(new UIImage(icon));

        return valueStack;
    }

}
*/
