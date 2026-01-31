package net.purple.solextended.client.gui.pages;

import net.minecraft.world.entity.player.Player;

import java.awt.*;

/**
 * Functional interface for lazy page creation.
 * External mods can register PageProviders to add custom pages to the Food Book.
 *
 * @see net.purple.solextended.client.gui.PageRegistry#registerExternalPage(String, PageProvider)
 */
@FunctionalInterface
public interface PageProvider {
    /**
     * Creates a new page instance.
     * Called lazily when the Food Book screen is opened.
     *
     * @param player The player viewing the book
     * @param frame  The rectangular frame/bounds for the page content area
     * @return A new Page instance
     */
    Page createPage(Player player, Rectangle frame);
}
