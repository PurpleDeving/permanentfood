package net.purple.permfood.config;

import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.api.SaveType;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import net.purple.permfood.Constants;


@Translation(prefix = "foodsystem.prefix")
public class FoodSystemConfig extends me.fzzyhmstrs.fzzy_config.config.Config {
    public FoodSystemConfig() {
        super(Constants.rLHungerConfig);
    }



    public HungerSection hunger = new HungerSection(); // hunger settings are stored here
    @Translation(prefix = "hunger.section")
    @Name("Hunger Settings")
    public static class HungerSection extends ConfigSection { // a Config Section. Self-serializable, and will add a "layer" to the GUI.


        public HungerSection() {
            super();
        }

        @RequiresAction(action = Action.RESTART)
        public boolean ENABLE_HUNGER_CHANGES = true;
        public int baseHunger = 20;
    }


    public SaturationSection saturation = new SaturationSection(); // hunger settings are stored here
    public static class SaturationSection extends ConfigSection { // a Config Section. Self-serializable, and will add a "layer" to the GUI.


        public SaturationSection() {
            super();
        }



        @RequiresAction(action = Action.RESTART)
        public boolean ENABLE_SATURATION_CHANGES = true;
    }

    public ExhaustionSection exhaustion = new ExhaustionSection(); // hunger settings are stored here
    public static class ExhaustionSection extends ConfigSection { // a Config Section. Self-serializable, and will add a "layer" to the GUI.

        public ExhaustionSection() {
            super();
        }


        @RequiresAction(action = Action.RESTART)
        public boolean ENABLE_EXHAUSTION_CHANGES = true;
    }


    // The Server should overwrite the clients config.
    @Override
    public SaveType saveType(){
        return SaveType.OVERWRITE;
    }

}
