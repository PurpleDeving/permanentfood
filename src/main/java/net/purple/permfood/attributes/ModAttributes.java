package net.purple.permfood.attributes;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.purple.permfood.Constants;
import net.purple.permfood.PermanentFood;

public class ModAttributes {


    /****************************************************************
     MAKE SURE: MAX VALUES HERE ARE LARGER THAN MAX IN CONFIG
     *****************************************************************/
    // TODO > Can you check that autoamtically ?


    /****************************************************************
     Attributes are loaded with Vanilla Defaults and set in EntityAttributeModificationEvent.
     Background: Server Configs are loaded to late to be used here. And a Startup Config would be seperate and not auto-synced
     *****************************************************************/


    private static final String descriptionIDPrefix = "attributes." + PermanentFood.MODID;

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(
            BuiltInRegistries.ATTRIBUTE, PermanentFood.MODID);

    public static final Holder<Attribute> MAX_HUNGER = ATTRIBUTES.register("max_hunger", () -> {

        int defaultvalue = Constants.VANILLA_MAX_HUNGER;

        return new RangedAttribute(
                // The translation key to use.
                descriptionIDPrefix + ".max_hunger",
                // The default value.
                defaultvalue,
                0,
                10000
        );

    });

    public static final Holder<Attribute> MAX_SATURATION = ATTRIBUTES.register("max_saturation", () -> {

        double defaultvalue = Constants.VANILLA_MAX_SATURATION;

        return new RangedAttribute(
                // The translation key to use.
                descriptionIDPrefix + ".max_saturation",
                // The default value.
                defaultvalue,
                0,
                10000
        );

    });


    public static final Holder<Attribute> MAX_EXHAUSTION = ATTRIBUTES.register("max_exhaustion", () -> {

        double defaultvalue = Constants.VANILLA_MAX_EXHAUSTION;

        return new RangedAttribute(
                // The translation key to use.
                descriptionIDPrefix + ".max_exhaustion",
                // The default value.
                defaultvalue,
                0,
                10000
        );

    });


    public static void register(IEventBus modEventBus) {
        ATTRIBUTES.register(modEventBus);
    }
}
