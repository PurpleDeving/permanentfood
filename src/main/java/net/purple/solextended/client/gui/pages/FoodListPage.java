package net.purple.solextended.client.gui.pages;

import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.purple.solextended.client.gui.elements.UIItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Page displaying a grid of item stacks (eaten foods).
 * Package-accessible for use by FoodBookScreen.
 */
@OnlyIn(Dist.CLIENT)
public final class FoodListPage extends Page {
    private static final int itemsPerRow = 5;
    private static final int rowsPerPage = 6;
    private static final int itemsPerPage = itemsPerRow * rowsPerPage;
    private static final int itemSpacing = UIItemStack.size + 4;

    /**
     * Creates multiple pages if needed to display all items.
     * Always returns at least one page, even when items list is empty.
     */
    public static List<FoodListPage> pages(Rectangle frame, String header, List<ItemStack> items) {
        List<FoodListPage> pages = new ArrayList<>();

        if (items.isEmpty()) {
            // Create an empty page with just the header
            pages.add(new FoodListPage(frame, header, items));
        } else {
            // Create pages for all items
            for (int startIndex = 0; startIndex < items.size(); startIndex += FoodListPage.itemsPerPage) {
                int endIndex = Math.min(startIndex + FoodListPage.itemsPerPage, items.size());
                pages.add(new FoodListPage(frame, header, items.subList(startIndex, endIndex)));
            }
        }

        return pages;
    }

    private FoodListPage(Rectangle frame, String header, List<ItemStack> items) {
        super(frame, header);

        int minX = (1 - itemsPerRow) * itemSpacing / 2;
        int minY = (1 - rowsPerPage) * itemSpacing / 2 - 4;

        for (int i = 0; i < items.size(); i++) {
            ItemStack itemStack = items.get(i);
            int x = minX + itemSpacing * (i % itemsPerRow);
            int y = minY + itemSpacing * ((i / itemsPerRow) % rowsPerPage);

            UIItemStack view = new UIItemStack(itemStack);
            view.setCenterX(getCenterX() + x);
            view.setCenterY(getCenterY() + y);
            children.add(view);
        }
    }
}
