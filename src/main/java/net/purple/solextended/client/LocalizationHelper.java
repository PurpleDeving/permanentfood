package net.purple.solextended.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import static net.purple.solextended.SolExtended.MODID;

public class LocalizationHelper {

    static MutableComponent localizedTooltip(String path, ChatFormatting color) {
        return localizedComponent("tooltip", path).withStyle(color);
    }

    /**
     * Overload allowing callers (e.g. permanentfood) to specify the modId explicitly.
     */
    public static MutableComponent localizedTooltip(String modId, String path, ChatFormatting color) {
        return localizedComponent(modId, "tooltip", path).withStyle(color);
    }

    public static MutableComponent localizedComponent(String domain, String path, Object... args) {
        return Component.translatable(keyString(domain, MODID, path), args);
    }

    /**
     * Overload allowing callers (e.g. permanentfood) to specify the modId explicitly.
     */
    public static MutableComponent localizedComponent(String modId, String domain, String path, Object... args) {
        return Component.translatable(keyString(modId, domain, path), args);
    }

    public static String keyString(String domain, String path) {
        return keyString(MODID, domain, path);
    }

    /**
     * Builds translation keys in the format: domain.modid.path
     */
    public static String keyString(String modId, String domain, String path) {
        return domain + "." + modId + "." + path;
    }
}
