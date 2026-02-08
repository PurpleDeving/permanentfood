/*
package backup.config;

import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.Translatable;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;
import net.purple.permfood.Constants;

import static net.purple.permfood.PermanentFood.MODID;

@Translatable.Name("Attribute System")
@Translatable.Name(value = "Attributsystem", lang = "de_de")
@Translatable.Desc("Some attribute caps in Minecraft may prevent values above a threshold; use an attribute-cap fix mod if needed.\n" +
        "See: https://minecraft.fandom.com/wiki/Attribute for info and https://www.curseforge.com/minecraft/mc-mods/attribute-fix for one mod  option.")

@Translation(prefix = MODID + ".attributesystem")
public class AttributeConfig extends Config {
    public AttributeConfig() {
        super(Constants.rLAttributeConfig);
    }

    //TODO - Add Health Stuff

    @Name("Armor Bonus Settings")
    @Name(value = "Rüstungsbonus Einstellungen", lang = "de_de")
    public ArmorSection sectionArmor = new ArmorSection();

    @Translation(prefix = "armor")
    public static class ArmorSection extends ConfigSection {

        @Desc("When true, this mod will grant armor attribute increases to the player based on milestones.")
        @RequiresAction(action = Action.RELOAD_DATA)
        public boolean ENABLE_ARMOR_CHANGES = true;

        @Desc("Amount of armor value added to the player for each milestone reached.")
        public ValidatedDouble ARMOR_PER_MILESTONE = new ValidatedDouble(2.0, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

        @Desc("Ascending list of numbers of unique foods required to reach each armor milestone.")
        public ValidatedList<Integer> MILESTONES_FOR_ARMOR = ValidatedList.ofInt(5, 10, 15, 20, 25);

        @Desc("Amount of armor toughness added to the player for each milestone reached.\n" +
                "If unfamiliar, see: https://minecraft.fandom.com/wiki/Armor#Armor_toughness")
        public ValidatedDouble ARMOR_TOUGHNESS_PER_MILESTONE = new ValidatedDouble(0.1, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

        @Desc("Ascending list of numbers of unique foods required to reach each armor-toughness milestone.")
        public ValidatedList<Integer> MILESTONES_FOR_ARMOR_TOUGHNESS = ValidatedList.ofInt(5, 10, 15, 20, 25);

    }

    @Name("Attack Damage Bonus Settings")
    @Name(value = "Angriffsschadenbonus Einstellungen", lang = "de_de")
    public AttackDamageSection sectionAttackDamage = new AttackDamageSection();

    @Translation(prefix = "attack_damage")
    public static class AttackDamageSection extends ConfigSection {

        @Desc("When true, this mod will grant attack damage increases to the player based on milestones.")
        @RequiresAction(action = Action.RELOAD_DATA)
        public boolean ENABLE_ATTACK_DAMAGE_CHANGES = true;

        @Desc("Amount of attack damage added to the player for each milestone reached. 1.0 equals half a heart.")
        public ValidatedDouble ATTACK_DAMAGE_PER_MILESTONE = new ValidatedDouble(1.0, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

        @Desc("Ascending list of numbers of unique foods required to reach each attack-damage milestone.")
        public ValidatedList<Integer> MILESTONES_FOR_ATTACK_DAMAGE = ValidatedList.ofInt(5, 10, 15, 20, 25);

    }

    @Name("Luck Bonus Settings")
    @Name(value = "Glücksbonus Einstellungen", lang = "de_de")
    public LuckSection sectionLuck = new LuckSection();

    @Translation(prefix = "luck")
    public static class LuckSection extends ConfigSection {

        @Desc("When true, this mod will grant Luck increases to the player based on milestones.")
        @RequiresAction(action = Action.RELOAD_DATA)
        public boolean ENABLE_LUCK_CHANGES = true;

        @Desc("Amount of Luck added to the player for each milestone reached. Positive values increase luck, negative decrease it.")
        public ValidatedDouble LUCK_PER_MILESTONE = new ValidatedDouble(1.0, 1000.0, -100.0, ValidatedNumber.WidgetType.TEXTBOX);

        @Desc("Ascending list of numbers of unique foods required to reach each luck milestone.")
        public ValidatedList<Integer> MILESTONES_FOR_LUCK = ValidatedList.ofInt(5, 10, 15, 20, 25);

    }

    @Name("Knockback Resistance Bonus Settings")
    @Name(value = "Rückstoßbeständigkeits Bonuseinstellungen", lang = "de_de")
    public KnockbackResistanceSection sectionKnockbackResistance = new KnockbackResistanceSection();

    @Translation(prefix = "knockback_resistance")
    public static class KnockbackResistanceSection extends ConfigSection {

        @Desc("When true, this mod will grant knockback resistance increases to the player based on milestones.")
        @RequiresAction(action = Action.RELOAD_DATA)
        public boolean ENABLE_KNOCKBACK_RESISTANCE_CHANGES = true;

        @Desc("Amount of generic_knockback_resistance added to the player for each milestone reached. Values should be in the range [0.0, 1.0] for percent-like resistance.")
        public ValidatedDouble KNOCKBACK_RESISTANCE_PER_MILESTONE = new ValidatedDouble(0.02, 1.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

        @Desc("Ascending list of numbers of unique foods required to reach each knockback-resistance milestone.")
        public ValidatedList<Integer> MILESTONES_FOR_KNOCKBACK_RESISTANCE = ValidatedList.ofInt(5, 10, 15, 20, 25);

    }

}
*/
