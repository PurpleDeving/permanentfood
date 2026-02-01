package net.purple.solextended.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.purple.solextended.SolExtended;
import net.purple.solextended.client.LocalizationHelper;
import net.purple.solextended.client.gui.elements.ImageData;
import net.purple.solextended.client.gui.elements.UIElement;
import net.purple.solextended.client.gui.elements.UIImage;
import net.purple.solextended.client.gui.elements.UILabel;
import net.purple.solextended.client.gui.pages.FoodListPage;
import net.purple.solextended.client.gui.pages.Page;
import net.purple.solextended.client.gui.pages.StatsPage;
import net.purple.solextended.foodlist.PlayerFoodList;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main Food Book GUI Screen.
 * Displays pages showing food statistics and eaten foods.
 * External mods can register custom pages via {@link PageRegistry#registerExternalPage(String, net.purple.solextended.client.gui.pages.PageProvider)}
 */
@OnlyIn(Dist.CLIENT)
public final class FoodBookScreen extends Screen implements PageFlipButton.Pageable {
    // ========================================
    // CONSTANTS & RESOURCES
    // ========================================
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            SolExtended.MODID, "textures/gui/food_book.png");

    private static final ImageData BOOK_IMAGE = new ImageData(TEXTURE,
            new Rectangle(0, 0, 186, 192));

    public static final ImageData CARROT_ICON = new ImageData(TEXTURE,
            new Rectangle(0, 240, 16, 16), 14, 14);

    // ========================================
    // UI COMPONENTS
    // ========================================
    private UIImage background;
    private UILabel pageNumberLabel;
    private UILabel modAttributionLabel;
    private final List<UIElement> permanentElements = new ArrayList<>();

    // ========================================
    // NAVIGATION
    // ========================================
    private PageFlipButton nextPageButton;
    private PageFlipButton prevPageButton;
    private List<PageRegistry.PageEntry> pageEntries = new ArrayList<>();
    private int currentPageNumber = 0;

    // ========================================
    // DATA
    // ========================================
    private Player player;
    private FoodData foodData;

    static {
        // Register internal pages
        registerInternalPages();
    }

    private static void registerInternalPages() {
        // First page: Stats
        PageRegistry.registerInternalPage((player, frame) -> {
            PlayerFoodList foodList = player.getData(SolExtended.FOOD_LIST_ATTACHMENT);
            FoodData data = new FoodData(foodList);
            return new StatsPage(data.getFoodsEatenCount(), frame);
        });

        // Last page: Eaten foods list
        PageRegistry.registerInternalPage((player, frame) -> {
            PlayerFoodList foodList = player.getData(SolExtended.FOOD_LIST_ATTACHMENT);
            FoodData data = new FoodData(foodList);

            List<ItemStack> eatenStacks = data.getEatenFoods().stream()
                    .map(ItemStack::new)
                    .collect(Collectors.toList());

            // ItemListPage.pages returns multiple pages if needed
            Component title = LocalizationHelper.localizedComponent("gui", "food_book.eaten_foods", eatenStacks.size());
            List<? extends Page> pages = FoodListPage.pages(frame,
                    title.getString(), eatenStacks);

            // Return the first page (we'll handle multiple pages separately)
            return pages.isEmpty() ? new StatsPage(0, frame) : pages.getFirst();
        });
    }

    // ========================================
    // INITIALIZATION
    // ========================================

    private FoodBookScreen(Player player) {
        super(Component.empty());
        this.player = player;
    }

    public static void open(Player player) {
        Minecraft.getInstance().setScreen(new FoodBookScreen(player));
    }

    @Override
    public void init() {
        super.init();

        // Load food data
        PlayerFoodList foodList = player.getData(SolExtended.FOOD_LIST_ATTACHMENT);
        foodData = new FoodData(foodList);

        // Setup background
        background = new UIImage(BOOK_IMAGE);
        background.setCenterX(width / 2);
        background.setCenterY(height / 2);

        permanentElements.clear();

        // Page number label
        pageNumberLabel = new UILabel(Component.literal("1"));
        pageNumberLabel.setCenterX(background.getCenterX());
        pageNumberLabel.setMinY(background.getMinY() + 156);
        permanentElements.add(pageNumberLabel);

        // Mod attribution label (initially hidden)
        modAttributionLabel = new UILabel(Component.empty());
        modAttributionLabel.color = new Color(128, 128, 128);
        modAttributionLabel.setMinX(background.getMinX() + 8);
        modAttributionLabel.setMaxY(background.getMaxY() - 8);

        // Initialize pages lazily
        initPages();

        // Setup page flip buttons
        int pageFlipButtonSpacing = 50;
        prevPageButton = addRenderableWidget(new PageFlipButton(
                background.getCenterX() - pageFlipButtonSpacing / 2 - PageFlipButton.width,
                background.getMinY() + 152,
                PageFlipButton.Direction.BACKWARD,
                this
        ));
        nextPageButton = addRenderableWidget(new PageFlipButton(
                background.getCenterX() + pageFlipButtonSpacing / 2,
                background.getMinY() + 152,
                PageFlipButton.Direction.FORWARD,
                this
        ));

        updateButtonVisibility();
        updateModAttribution();
    }

    private void initPages() {
        pageEntries.clear();

        // Create pages from registry - this handles the proper ordering:
        // 1. First internal page (stats)
        // 2. External pages (in registration order)
        // 3. Eaten foods list pages

        List<PageRegistry.PageEntry> registryPages = PageRegistry.createAllPages(player, background.frame);

        // For the last internal page (eaten foods), we need to handle pagination
        if (!registryPages.isEmpty()) {
            // Add all pages except the last one (which might be multi-page)
            for (int i = 0; i < registryPages.size() - 1; i++) {
                pageEntries.add(registryPages.get(i));
            }

            // Handle the eaten foods list (potentially multiple pages)
            PageRegistry.PageEntry lastEntry = registryPages.get(registryPages.size() - 1);
            List<ItemStack> eatenStacks = foodData.getEatenFoods().stream()
                    .map(ItemStack::new)
                    .collect(Collectors.toList());

            if (!eatenStacks.isEmpty()) {
                Component title = LocalizationHelper.localizedComponent("gui", "food_book.eaten_foods", eatenStacks.size());
                List<? extends Page> foodPages = FoodListPage.pages(background.frame,
                        title.getString(), eatenStacks);

                for (Page page : foodPages) {
                    pageEntries.add(new PageRegistry.PageEntry(page, null));
                }
            } else {
                // No eaten foods, just add the placeholder page
                pageEntries.add(lastEntry);
            }
        }
    }

    // ========================================
    // RENDERING
    // ========================================

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);

        if (!pageEntries.isEmpty()) {
            // Render permanent UI elements
            UIElement.render(graphics, permanentElements, mouseX, mouseY);

            // Render current page
            PageRegistry.PageEntry currentEntry = pageEntries.get(currentPageNumber);
            UIElement.render(graphics, currentEntry.page, mouseX, mouseY);

            // Render mod attribution if external page
            if (currentEntry.isExternal()) {
                UIElement.render(graphics, modAttributionLabel, mouseX, mouseY);
            }
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        UIElement.render(guiGraphics, background, mouseX, mouseY);
    }

    // ========================================
    // PAGE NAVIGATION
    // ========================================

    @Override
    public void switchToPage(int pageNumber) {
        if (!isWithinRange(pageNumber)) return;

        currentPageNumber = pageNumber;
        updateButtonVisibility();
        updateModAttribution();

        pageNumberLabel.setComponent(Component.literal("" + (currentPageNumber + 1)));
    }

    @Override
    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    @Override
    public boolean isWithinRange(int pageNumber) {
        return pageNumber >= 0 && pageNumber < pageEntries.size();
    }

    private void updateButtonVisibility() {
        prevPageButton.updateState();
        nextPageButton.updateState();
    }

    private void updateModAttribution() {
        if (pageEntries.isEmpty()) return;

        PageRegistry.PageEntry currentEntry = pageEntries.get(currentPageNumber);
        if (currentEntry.isExternal()) {
            modAttributionLabel.setComponent(LocalizationHelper.localizedComponent("gui", "food_book.attribution", currentEntry.modId));
            // Update label frame to fit new text
            modAttributionLabel.frame.width = font.width(modAttributionLabel.component) - 1;
        } else {
            modAttributionLabel.setComponent(Component.empty());
        }
    }
}
