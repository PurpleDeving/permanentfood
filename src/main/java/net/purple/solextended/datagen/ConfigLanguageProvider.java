package net.purple.solextended.datagen;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.purple.permfood.Constants;
import net.purple.solextended.config.SolClientConfig;
import net.purple.solextended.config.SolExtendedConfig;

import static net.purple.solextended.SolExtended.MODID;


public class ConfigLanguageProvider extends LanguageProvider {

    private final String locale;

    public ConfigLanguageProvider(PackOutput output, String locale) {
        super(output, MODID, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {

        ConfigApiJava.buildTranslations(SolClientConfig.class, Constants.rLFoodSystemConfig, this.locale, true, super::add);
        ConfigApiJava.buildTranslations(SolExtendedConfig.class, Constants.rLFoodSystemConfig, this.locale, true, super::add);

    }

}
