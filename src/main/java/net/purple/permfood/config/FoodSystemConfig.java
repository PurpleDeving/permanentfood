package net.purple.permfood.config;

import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.api.SaveType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.Translatable;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedChoice;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;
import net.minecraft.world.Difficulty;
import net.purple.permfood.Constants;
import org.jetbrains.annotations.NotNull;

@Translatable.Name("Food System")
@Translatable.Name(value = "Ernährungssystem", lang = "de_de")
public class FoodSystemConfig extends Config {

    // TODO - English is fixed but not tested. German/ Test not tested yet

    public FoodSystemConfig() {
        super(Constants.rLFoodSystemConfig);
    }

    @Name("Peaceful Hunger Settings")
    @Name(value = "Friedliche Hunger Einstellungen", lang = "de_de")

    public PeacefulHungerSection sectionPeacefulHunger = new PeacefulHungerSection();

    public static class PeacefulHungerSection extends ConfigSection {

        @Name("Enable hunger on Peaceful")
        @Name(value = "Hunger auf Friedlich aktivieren", lang = "de_de")
        @Desc("When true, players will still get hungry on Peaceful difficulty. Set to false to disable hunger on Peaceful.")
        public boolean ENABLE_HUNGER_ON_PEACEFUL = true;

        @Name("Peaceful hunger difficulty")
        @Name(value = "Schwierigkeit für Hunger auf Friedlich", lang = "de_de")
        @Desc("When hunger on Peaceful is enabled, this sets which difficulty's hunger rules to apply (EASY, NORMAL, or HARD).")
        public ValidatedChoice<Difficulty> PEACEFUL_HUNGER_DIFFICULTY = new ValidatedEnum<>(Difficulty.EASY).toList(Difficulty.EASY, Difficulty.NORMAL, Difficulty.HARD).toChoices(ValidatedChoice.WidgetType.CYCLING);

        @Name("Regen threshold (with saturation)")
        @Name(value = "Regeneration-Schwelle (mit Sättigung)", lang = "de_de")
        @Desc("Percentage of the hunger bar required for strong natural regeneration when the player has saturation (vanilla: 100).\n" +
                "Requires the game rule 'naturalRegeneration' to be enabled. Values are rounded down.")
        public ValidatedInt NATURAL_REGEN_THRESHOLD_WITH_SATURATION = new ValidatedInt(100, 100, 0);

        @Name("Regen threshold (no saturation)")
        @Name(value = "Regeneration-Schwelle (ohne Sättigung)", lang = "de_de")
        @Desc("Percentage of the hunger bar required for weak natural regeneration when the player has no saturation (vanilla: 90).\n" +
                "Requires the game rule 'naturalRegeneration' to be enabled. Values are rounded down.")
        public ValidatedInt NATURAL_REGEN_THRESHOLD_NO_SATURATION = new ValidatedInt(90, 100, 0);


    }

    @Name("Max Hunger Settings")
    @Name(value = "Hunger Einstellungen", lang = "de_de")
    public HungerSection sectionHunger = new HungerSection(); // hunger settings are stored here


    public static class HungerSection extends ConfigSection { // a Config Section. Self-serializable, and will add a "layer" to the GUI.
        public HungerSection() {
            super();
        }

        @Name("Enable Hunger Changes")
        @Desc("Whenever or not hunger mechanics should be changed from this mod.")
        @RequiresAction(action = Action.RESTART)
        public boolean ENABLE_HUNGER_CHANGES = true;

        @Name("Base max hunger")
        @Name(value = "Basis-Maximalhunger", lang = "de_de")
        @Desc("The starting maximum hunger before any milestones are reached.")
        @RequiresAction(action = Action.RESTART)
        public ValidatedInt baseHunger = new ValidatedInt(20, 10000, 0, ValidatedNumber.WidgetType.TEXTBOX);

        @Name("Hunger per milestone")
        @Name(value = "Hunger pro Meilenstein", lang = "de_de")
        @Desc("The Amount of max Hunger gained per Milestone reached. \n" +
                "Higher values will make each Milestone more impactful.")
        public ValidatedInt perMilestoneHunger = new ValidatedInt(2, 100, 0, ValidatedNumber.WidgetType.TEXTBOX);

        @Name("Hunger milestones")
        @Name(value = "Hunger-Meilensteine", lang = "de_de")
        @Desc("The Amount of unique Foods eaten needed to reach the different Milestones. \n" +
                "More Milestones will let you earn more max hunger.\n" +
                "Must be in order from smallest to largest.")
        public ValidatedList<Integer> milestonesHunger = ValidatedList.ofInt(5, 10, 15, 20);
    }


    @Name("Max Saturation Settings")
    @Name(value = "Sättigungseinstellungen", lang = "de_de")
    public SaturationSection sectionSaturation = new SaturationSection();


    public static class SaturationSection extends ConfigSection {


        public SaturationSection() {
            super();
        }

        @Name("Enable saturation changes")
        @Name(value = "Sättigungsänderungen aktivieren", lang = "de_de")
        @Desc("Whether or not saturation mechanics should be changed by this mod.")
        @RequiresAction(action = Action.RESTART)
        public boolean ENABLE_SATURATION_CHANGES = true;

        @Name("Base max saturation")
        @Name(value = "Basis-Maximalsättigung", lang = "de_de")
        @Desc("The starting maximum saturation before any milestones are reached.")
        @RequiresAction(action = Action.RESTART)
        public ValidatedFloat baseSaturation = new ValidatedFloat(20.0F, 10000.0F, 0, ValidatedNumber.WidgetType.TEXTBOX);

        @Name("Saturation per milestone")
        @Name(value = "Sättigung pro Meilenstein", lang = "de_de")
        @Desc("The Amount of max Saturation gained per Milestone reached. \n" +
                "Higher values will make each Milestone more impactful.")
        public ValidatedFloat perMilestoneSaturation = new ValidatedFloat(2.0F, 100, 0, ValidatedNumber.WidgetType.TEXTBOX);

        @Name("Saturation milestones")
        @Name(value = "Sättigungs-Meilensteine", lang = "de_de")
        @Desc("The Amount of unique Foods eaten needed to reach the different Milestones. \n" +
                "More Milestones will let you earn more max Saturation.\n" +
                "Must be in order from smallest to largest.")
        public ValidatedList<Integer> milestonesSaturation = ValidatedList.ofInt(5, 10, 15, 20);
    }


    @Name("Max Exhaustion Settings")
    @Name(value = "Erschöpfungseinstellungen", lang = "de_de")
    public ExhaustionSection sectionExhaustion = new ExhaustionSection();


    public static class ExhaustionSection extends ConfigSection {

        public ExhaustionSection() {
            super();
        }


        @Name("Enable exhaustion changes")
        @Name(value = "Erschöpfungsänderungen aktivieren", lang = "de_de")
        @Desc("Whether or not exhaustion mechanics should be changed by this mod.")
        @RequiresAction(action = Action.RESTART)
        public boolean ENABLE_EXHAUSTION_CHANGES = true;

        @Name("Base max exhaustion")
        @Name(value = "Basis-Maximalerschöpfung", lang = "de_de")
        @Desc("The starting maximum exhaustion before any milestones are reached.")
        @RequiresAction(action = Action.RESTART)
        public ValidatedFloat baseExhaustion = new ValidatedFloat(4.0F, 10000, 0, ValidatedNumber.WidgetType.TEXTBOX);

        @Name("Exhaustion per milestone")
        @Name(value = "Erschöpfung pro Meilenstein", lang = "de_de")
        @Desc("The Amount of max Exhaustion gained per Milestone reached. \n" +
                "Higher values will make each Milestone more impactful.")
        public ValidatedFloat perMilestoneExhaustion = new ValidatedFloat(2.0F, 100, 0, ValidatedNumber.WidgetType.TEXTBOX);

        @Name("Exhaustion milestones")
        @Name(value = "Erschöpfungs-Meilensteine", lang = "de_de")
        @Desc("The Amount of unique Foods eaten needed to reach the different Milestones. \n" +
                "More Milestones will let you earn more max Exhaustion.\n" +
                "Must be in order from smallest to largest.")
        public ValidatedList<Integer> milestonesExhaustion = ValidatedList.ofInt(5, 10, 15, 20);


        @Name("Exhaustion per heal")
        @Name(value = "Erschöpfung pro Heilung", lang = "de_de")
        @Desc("Amount of exhaustion applied to the player when they heal one heart. Higher values increase exhaustion cost for healing. Vanilla default is 6.0.")
        @RequiresAction(action = Action.RESTART)
        public ValidatedFloat exhaustion_per_Heal = new ValidatedFloat(6.0F, 1000.0F, 0.1F, ValidatedNumber.WidgetType.TEXTBOX);
    }


    // The Server should overwrite the client's config.
    @Override
    public @NotNull SaveType saveType() {
        return SaveType.OVERWRITE;
    }

}
