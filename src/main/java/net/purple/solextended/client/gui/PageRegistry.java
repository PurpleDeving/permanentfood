package net.purple.solextended.client.gui;

import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.purple.solextended.client.gui.pages.Page;
import net.purple.solextended.client.gui.pages.PageProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry for Food Book pages.
 * Manages both internal (core) pages and external (mod-added) pages.
 * External mods can register custom pages that will be displayed between
 * the stats page and the eaten foods list.
 */
@OnlyIn(Dist.CLIENT)
public final class PageRegistry {
    private static final List<PageProvider> internalPages = new ArrayList<>();
    private static final Map<String, PageProvider> externalPages = new LinkedHashMap<>();

    private PageRegistry() {
        // Prevent instantiation
    }

    /**
     * Registers an internal core page.
     * Only for use by permanentfood itself.
     *
     * @param provider The page provider
     */
    static void registerInternalPage(PageProvider provider) {
        internalPages.add(provider);
    }

    /**
     * Registers an external page from another mod.
     * External pages will be displayed between the stats page and the eaten foods list,
     * in the order they were registered. They will show the mod name in the corner.
     *
     * @param modId    The mod ID that is registering this page
     * @param provider The page provider that creates the page
     */
    public static void registerExternalPage(String modId, PageProvider provider) {
        if (modId == null || modId.isEmpty()) {
            throw new IllegalArgumentException("Mod ID cannot be null or empty");
        }
        if (provider == null) {
            throw new IllegalArgumentException("Page provider cannot be null");
        }
        externalPages.put(modId, provider);
    }

    /**
     * Creates all registered pages in the correct order:
     * 1. First internal page (stats)
     * 2. External pages (in registration order)
     * 3. Last internal page (eaten foods list)
     *
     * @param player The player viewing the book
     * @param frame  The page content area frame
     * @return List of page entries with mod attribution
     */
    static List<PageEntry> createAllPages(Player player, Rectangle frame) {
        List<PageEntry> allPages = new ArrayList<>();

        // Add first internal page (stats page)
        if (!internalPages.isEmpty()) {
            Page page = internalPages.get(0).createPage(player, frame);
            allPages.add(new PageEntry(page, null));
        }

        // Add external pages in registration order
        for (Map.Entry<String, PageProvider> entry : externalPages.entrySet()) {
            Page page = entry.getValue().createPage(player, frame);
            allPages.add(new PageEntry(page, entry.getKey()));
        }

        // Add remaining internal pages (eaten foods list)
        for (int i = 1; i < internalPages.size(); i++) {
            Page page = internalPages.get(i).createPage(player, frame);
            allPages.add(new PageEntry(page, null));
        }

        return allPages;
    }

    /**
     * Clears all registered pages. For testing purposes.
     */
    static void clearAll() {
        internalPages.clear();
        externalPages.clear();
    }

    /**
     * Page entry with optional mod attribution.
     */
    static class PageEntry {
        final Page page;
        final String modId; // null for internal pages

        PageEntry(Page page, String modId) {
            this.page = page;
            this.modId = modId;
        }

        boolean isExternal() {
            return modId != null;
        }
    }
}
