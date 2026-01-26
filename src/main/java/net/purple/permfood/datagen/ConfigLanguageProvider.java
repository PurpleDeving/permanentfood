package net.purple.permfood.datagen;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.purple.permfood.Constants;
import net.purple.permfood.config.FoodSystemConfig;

import static net.purple.permfood.PermanentFood.MODID;

public class ConfigLanguageProvider extends LanguageProvider {

    private final String locale;

    public ConfigLanguageProvider(PackOutput output, String locale) {
        super(output, MODID, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {

        ConfigApiJava.buildTranslations(FoodSystemConfig.class, Constants.rLFoodSystemConfig, this.locale, true, super::add);

    }

}
