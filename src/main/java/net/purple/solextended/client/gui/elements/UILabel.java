package net.purple.solextended.client.gui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.awt.*;

/**
 * UI element for rendering text labels.
 * Public API for external mods.
 */
public class UILabel extends UIElement {
    public String text = "";
    public Component component = Component.empty();
    public TextAlignment alignment = TextAlignment.CENTER;
    public Color color = Color.BLACK;

    /**
     * sets frame to text size
     */
    public UILabel(String text) {
        this(Component.literal(text));
    }

    /**
     * sets frame to text size
     */
    public UILabel(Component component) {
        this(new Rectangle(font.width(component) - 1, 7), component);
    }

    public UILabel(Rectangle frame, String text) {
        this(frame, Component.literal(text));
    }

    public UILabel(Rectangle frame, Component component) {
        super(frame);
        setComponent(component);
    }

    public UILabel(Rectangle frame) {
        super(frame);
        setComponent(Component.empty());
    }

    public void setComponent(Component component) {
        this.component = component == null ? Component.empty() : component;
        this.text = this.component.getString();
    }

    @Override
    protected void render(GuiGraphics graphics) {
        super.render(graphics);

        int textWidth = font.width(component) - 1;
        int x = frame.x + (frame.width - textWidth) * alignment.ordinal / 2;
        int y = frame.y + (frame.height - 7) / 2;
        if (color.getTransparency() == Color.TRANSLUCENT) {
            RenderSystem.enableBlend();
        }
        graphics.drawString(font, component, x, y, color.getRGB(), false);
    }

    public enum TextAlignment {
        LEFT(0), CENTER(1), RIGHT(2);

        final int ordinal;

        TextAlignment(int ordinal) {
            this.ordinal = ordinal;
        }
    }
}
