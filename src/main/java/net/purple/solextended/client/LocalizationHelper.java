package net.purple.solextended.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class LocalizationHelper {


    /**
     * Overload allowing callers (e.g. permanentfood) to specify the modId explicitly.
     */
    public static MutableComponent localizedTooltip(String modId, String path, ChatFormatting color) {
        return localizedComponent(modId, "tooltip", path).withStyle(color);
    }


    /**
     * Overload allowing callers (e.g. permanentfood) to specify the modId explicitly.
     */
    public static MutableComponent localizedComponent(String modId, String domain, String path, Object... args) {
        return Component.translatable(keyString(modId, domain, path), args);
    }


    /**
     * Builds translation keys in the format: domain.modid.path
     */
    public static String keyString(String modId, String domain, String path) {
        return domain + "." + modId + "." + path;
    }
}
