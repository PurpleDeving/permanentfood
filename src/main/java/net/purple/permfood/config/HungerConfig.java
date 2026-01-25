package net.purple.permfood.config;

import me.fzzyhmstrs.fzzy_config.api.SaveType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import net.minecraft.resources.ResourceLocation;
import net.purple.permfood.Constants;
import org.jetbrains.annotations.NotNull;

public class HungerConfig extends me.fzzyhmstrs.fzzy_config.config.Config {
    public HungerConfig() {
        super(Constants.rLHungerConfig);
    }



    public HungerSection hunger = new HungerSection(); // hunger settings are stored here
    public static class HungerSection extends ConfigSection { // a Config Section. Self-serializable, and will add a "layer" to the GUI.


        public HungerSection() {
            super();
        }

        public boolean ENABLE_HUNGER_CHANGES = true;
        public int baseHunger = 20;
    }

    public SaturationSection saturation = new SaturationSection(); // hunger settings are stored here
    public static class SaturationSection extends ConfigSection { // a Config Section. Self-serializable, and will add a "layer" to the GUI.


        public SaturationSection() {
            super();
        }

        public boolean ENABLE_SATURATION_CHANGES = true;
    }

    public ExhaustionSection exhaustion = new ExhaustionSection(); // hunger settings are stored here
    public static class ExhaustionSection extends ConfigSection { // a Config Section. Self-serializable, and will add a "layer" to the GUI.


        public ExhaustionSection() {
            super();
        }

        public boolean ENABLE_EXHAUSTION_CHANGES = true;
    }


    // The Server should overwrite the clients config.
    @Override
    public SaveType saveType(){
        return SaveType.OVERWRITE;
    }

}
