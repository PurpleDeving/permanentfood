package net.purple.permfood.config;

import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.api.SaveType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.Translatable;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
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

    // TODO - Recreate these + Generate the german tests again

    public FoodSystemConfig() {
        super(Constants.rLFoodSystemConfig);
    }

    @Name("Hunger Difficulty Settings")
    @Name(value = "Hunger-Schwierigkeitseinstellungen", lang = "de_de")

    public FoodHealingSection sectionFoodHealing = new FoodHealingSection();

    public static class FoodHealingSection extends ConfigSection {

        @Name("Enable separate hunger difficulty")
        @Name(value = "Separate Hunger-Schwierigkeit aktivieren", lang = "de_de")
        @Desc("When true, a separate difficulty is used for food and hunger. This allows hunger difficulty to be independent from the game's difficulty setting.")
        public boolean ENABLE_HUNGER_DIFFICULTY = true;

        @Name("Hunger difficulty")
        @Name(value = "Hunger-Schwierigkeit", lang = "de_de")
        @Desc("When separate hunger difficulty is enabled, this sets which difficulty's hunger rules to apply.")
        public ValidatedEnum<Difficulty> HUNGER_DIFFICULTY = new ValidatedEnum<>(Difficulty.EASY, ValidatedEnum.WidgetType.CYCLING);


        @Name("Exhaustion per heal")
        @Name(value = "Erschöpfung pro Heilung", lang = "de_de")
        @Desc("Amount of exhaustion applied to the player when they heal one heart. Lower is 'easier' for the player. Vanilla default is 6.0.")
        @RequiresAction(action = Action.RESTART)
        public ValidatedFloat exhaustionPerHeal = new ValidatedFloat(6.0F, 1000.0F, 0.1F, ValidatedNumber.WidgetType.TEXTBOX);


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
        @Desc("""
                The Amount of unique Foods eaten needed to reach the different Milestones.
                More Milestones will let you earn more max hunger.
                Must be in order from smallest to largest.""")
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
        @Desc("""
                The Amount of unique Foods eaten needed to reach the different Milestones.
                More Milestones will let you earn more max Saturation.
                Must be in order from smallest to largest.""")
        public ValidatedList<Integer> milestonesSaturation = ValidatedList.ofInt(5, 10, 15, 20);


    }


    @Name("Max Exhaustion Settings")
    @Name(value = "Erschöpfungseinstellungen", lang = "de_de")
    public ExhaustionSection sectionExhaustion = new ExhaustionSection();


    public static class ExhaustionSection extends ConfigSection {

        public ExhaustionSection() {
            super();
        }


        // Add that higher max exhaustion is good for baseExhaustion and perMilestoneExhaustion. The value is the threshold before saturation/hunger is deducted
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
        public ValidatedFloat perMilestoneExhaustion = new ValidatedFloat(0.20F, 100, 0, ValidatedNumber.WidgetType.TEXTBOX);

        @Name("Exhaustion milestones")
        @Name(value = "Erschöpfungs-Meilensteine", lang = "de_de")
        @Desc("""
                The Amount of unique Foods eaten needed to reach the different Milestones.\s
                More Milestones will let you earn more max Exhaustion.
                Must be in order from smallest to largest.""")
        public ValidatedList<Integer> milestonesExhaustion = ValidatedList.ofInt(5, 10, 15, 20);


    }


    // The Server should overwrite the client's config.
    @Override
    public @NotNull SaveType saveType() {
        return SaveType.OVERWRITE;
    }

}
