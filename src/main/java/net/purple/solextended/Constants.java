package net.purple.solextended;

import net.minecraft.resources.ResourceLocation;

import static net.purple.solextended.SolExtended.IS_DEV;
import static net.purple.solextended.SolExtended.MODID;

@SuppressWarnings("PointlessBooleanExpression")
public class Constants {


    // Configs
    public static ResourceLocation rLSolExtendedConfig = ResourceLocation.fromNamespaceAndPath(MODID, "sol_extended_config");
    public static ResourceLocation rLSolClientConfig = ResourceLocation.fromNamespaceAndPath(MODID, "sol_client_config");

    // GUI Textures
    public static ResourceLocation rLFoodBookItemGUI = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/food_book.png");


    // DEBUGGING & TESTING

    // Enable test items in Food Book GUI
    public static final boolean FOOD_BOOK_TEST_ITEMS = IS_DEV && true; //TODO Need to Test

}
