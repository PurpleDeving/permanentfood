package net.purple.permfood.config;

import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.Translatable;
import net.purple.permfood.Constants;

import static net.purple.permfood.PermanentFood.MODID;

@Translatable.Name("Attribute System")
@Translatable.Name(value = "Attributsystem", lang = "de_de")
@Translatable.Desc("Some attribute caps in Minecraft may prevent values above a threshold; use an attribute-cap fix mod if needed.\n" +
        "See: https://minecraft.fandom.com/wiki/Attribute for info and https://www.curseforge.com/minecraft/mc-mods/attribute-fix for one mod  option.")

@Translation(prefix = MODID + ".attributesystem")
public class AttributesConfig extends Config {
    public AttributesConfig() {
        super(Constants.rLAttributeConfig);
    }

    @Name("Armor Bonus Settings")
    @Name(value = "Rüstungsbonus Einstellungen", lang = "de_de")
    public ArmorSection sectionArmor = new ArmorSection();

    @Translation(prefix = "peaceful_hunger.")
    public static class ArmorSection extends ConfigSection {


        @Desc("When true, food armor buffs are enabled.")
        @RequiresAction(action = Action.RELOAD_DATA)
        public boolean ENABLE_ARMOR_BUFF = true;


    }


}
