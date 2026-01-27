package backup.client;

import net.minecraft.client.resources.language.I18n;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static net.purple.permfood.PermanentFood.MODID;

public class LocalizationHelper {

    @OnlyIn(Dist.CLIENT)
    public static String localized(String domain, String path, Object... args) {
        return I18n.get(
                (domain + "." + MODID + "." + path), args);
    }

}
