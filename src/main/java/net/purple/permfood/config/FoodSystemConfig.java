package net.purple.permfood.config;

import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.api.SaveType;
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

import static net.purple.permfood.PermanentFood.MODID;

@Translatable.Name("Food System")
@Translatable.Name(value = "Ernährungssystem", lang = "de_de")
@Translation(prefix = MODID + ".foodsystem")
public class FoodSystemConfig extends me.fzzyhmstrs.fzzy_config.config.Config {


    public FoodSystemConfig() {
        super(Constants.rLFoodSystemConfig);
    }


    @Name("Peaceful Hunger Settings")
    @Name(value = "Friedliche Hunger Einstellungen", lang = "de_de")
    public PeacefulHungerSection peacefulHungerSection = new PeacefulHungerSection();

    @Translation(prefix = "peaceful_hunger.")
    public static class PeacefulHungerSection extends ConfigSection {

        @Desc("When true, players will still get hungry on Peaceful difficulty. Set to false to disable hunger on Peaceful.")
        public boolean ENABLE_HUNGER_ON_PEACEFUL = true;

        @Desc("When hunger on Peaceful is enabled, this sets which difficulty's hunger rules to apply (EASY, NORMAL, or HARD).")
        public ValidatedChoice<Difficulty> PEACEFUL_HUNGER_DIFFICULTY = new ValidatedEnum<>(Difficulty.EASY).toList(Difficulty.EASY, Difficulty.NORMAL, Difficulty.HARD).toChoices(ValidatedChoice.WidgetType.CYCLING);

        @Desc("Percentage of the hunger bar required for strong natural regeneration when the player has saturation (vanilla: 100).\n" +
                "Requires the game rule 'naturalRegeneration' to be enabled. Values are rounded down.")
        public ValidatedInt NATURAL_REGEN_THRESHOLD_WITH_SATURATION = new ValidatedInt(100, 100, 0);
        @Desc("Percentage of the hunger bar required for weak natural regeneration when the player has no saturation (vanilla: 90).\n" +
                "Requires the game rule 'naturalRegeneration' to be enabled. Values are rounded down.")
        public ValidatedInt NATURAL_REGEN_THRESHOLD_NO_SATURATION = new ValidatedInt(90, 100, 0);


    }


    @Name("Max Hunger Settings")
    @Name(value = "Hunger Einstellungen", lang = "de_de")
    public HungerSection sectionHunger = new HungerSection(); // hunger settings are stored here

    @Translation(prefix = "max_hunger.")
    public static class HungerSection extends ConfigSection { // a Config Section. Self-serializable, and will add a "layer" to the GUI.


        public HungerSection() {
            super();
        }

        @RequiresAction(action = Action.RESTART)
        public boolean ENABLE_HUNGER_CHANGES = true;

        @RequiresAction(action = Action.RESTART)
        public ValidatedInt baseHunger = new ValidatedInt(20, 10000, 0, ValidatedNumber.WidgetType.TEXTBOX);

        @Desc("The Amount of max Hunger gained per Milestone reached. \n" +
                "Higher values will make each Milestone more impactful.")
        public ValidatedInt perMilestoneHunger = new ValidatedInt(2, 100, 0, ValidatedNumber.WidgetType.TEXTBOX);

        @Desc("The Amount of unique Foods eaten needed to reach the different Milestones. \n" +
                "More Milestones will let you earn more max hunger.\n" +
                "Must be in order from smallest to largest.")
        public ValidatedList<Integer> milestonesHunger = ValidatedList.ofInt(5, 10, 15, 20);
    }

    @Name("Max Saturation Settings")
    @Name(value = "Sättigungseinstellungen", lang = "de_de")
    public SaturationSection sectionSaturation = new SaturationSection();

    @Translation(prefix = "max_saturation.")
    public static class SaturationSection extends ConfigSection {


        public SaturationSection() {
            super();
        }

        @RequiresAction(action = Action.RESTART)
        public boolean ENABLE_SATURATION_CHANGES = true;

        @RequiresAction(action = Action.RESTART)
        public ValidatedFloat baseSaturation = new ValidatedFloat(4.0F, 10000.0F, 0, ValidatedNumber.WidgetType.TEXTBOX);

        @Desc("The Amount of max Saturation gained per Milestone reached. \n" +
                "Higher values will make each Milestone more impactful.")
        public ValidatedFloat perMilestoneSaturation = new ValidatedFloat(2.0F, 100, 0, ValidatedNumber.WidgetType.TEXTBOX);

        @Desc("The Amount of unique Foods eaten needed to reach the different Milestones. \n" +
                "More Milestones will let you earn more max Saturation.\n" +
                "Must be in order from smallest to largest.")
        public ValidatedList<Integer> milestonesSaturation = ValidatedList.ofInt(5, 10, 15, 20);
    }

    @Name("Max Exhaustion Settings")
    @Name(value = "Erschöpfungseinstellungen", lang = "de_de")
    public ExhaustionSection sectionExhaustion = new ExhaustionSection();

    @Translation(prefix = "max_exhaustion.")
    public static class ExhaustionSection extends ConfigSection {

        public ExhaustionSection() {
            super();
        }


        @RequiresAction(action = Action.RESTART)
        public boolean ENABLE_EXHAUSTION_CHANGES = true;

        @RequiresAction(action = Action.RESTART)
        public ValidatedFloat baseExhaustion = new ValidatedFloat(20.0F, 10000, 0, ValidatedNumber.WidgetType.TEXTBOX);

        @Desc("The Amount of max Exhaustion gained per Milestone reached. \n" +
                "Higher values will make each Milestone more impactful.")
        public ValidatedFloat perMilestoneSaturation = new ValidatedFloat(2.0F, 100, 0, ValidatedNumber.WidgetType.TEXTBOX);

        @Desc("The Amount of unique Foods eaten needed to reach the different Milestones. \n" +
                "More Milestones will let you earn more max Exhaustion.\n" +
                "Must be in order from smallest to largest.")
        public ValidatedList<Integer> milestonesSaturation = ValidatedList.ofInt(5, 10, 15, 20);
    }


    // The Server should overwrite the clients config.
    @Override
    public SaveType saveType() {
        return SaveType.OVERWRITE;
    }

}
