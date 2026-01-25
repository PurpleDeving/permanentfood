package net.purple.permfood.datagen;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.purple.permfood.Constants;
import net.purple.permfood.config.FoodSystemConfig;

import static net.purple.permfood.PermanentFood.MODID;

public class ConfigLanguageProvider extends LanguageProvider {

    public ConfigLanguageProvider(PackOutput output) {
        super(output, MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

        ConfigApiJava.buildTranslations(FoodSystemConfig.class, Constants.rLHungerConfig, "en_us", true, super::add);
    }

}
