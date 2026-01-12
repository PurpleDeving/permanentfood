package net.purple.permfood.util;

import java.awt.Rectangle;
import net.purple.permfood.ui.PlayerValuesUIPage;

/**
 * Factory used by the mixin to create a page-like UIElement.
 * Placed outside of the mixin package to avoid Mixin classloader restrictions.
 */
public final class PlayerValuesPage {
    private PlayerValuesPage() { }

    public static Object create(Rectangle frame) {
        try {
            return new PlayerValuesUIPage(frame);
        } catch (Throwable t) {
            return null;
        }
    }
}

