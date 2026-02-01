package net.purple.solextended.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import static net.purple.solextended.SolExtended.MODID;

public class LocalizationHelper {
    
    static MutableComponent localizedTooltip(String path, ChatFormatting color) {
        return localizedComponent("tooltip", path).withStyle(color);
    }

    public static MutableComponent localizedComponent(String domain, String path, Object... args) {
        return Component.translatable(keyString(domain, path), args);
    }

    public static String keyString(String domain, String path) {
        return domain + "." + MODID + "." + path;
    }
}
