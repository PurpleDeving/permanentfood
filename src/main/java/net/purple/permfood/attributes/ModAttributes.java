package net.purple.permfood.attributes;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.purple.permfood.PermanentFood;
import net.purple.permfood.config.Configs;

public class ModAttributes {


    /****************************************************************
     MAKE SURE: MAX VALUES HERE ARE LARGER THAN MAX IN CONFIG
     *****************************************************************/
    // TODO > Can you check that automatically ?


    /****************************************************************
     Attributes are conditionally registered based on FoodSystemConfig enable flags.
     They are loaded with Vanilla Defaults and set in EntityAttributeModificationEvent.
     *****************************************************************/


    private static final String descriptionIDPrefix = "attributes." + PermanentFood.MODID;

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(
            BuiltInRegistries.ATTRIBUTE, PermanentFood.MODID);

    public static final Holder<Attribute> MAX_HUNGER = Configs.foodSystemConfig.sectionHunger.ENABLE_HUNGER_CHANGES
            ? ATTRIBUTES.register("max_hunger", () -> {

        int defaultvalue = Configs.foodSystemConfig.sectionHunger.baseHunger.get();

        return new RangedAttribute(
                // The translation key to use.
                descriptionIDPrefix + ".max_hunger",
                // The default value.
                defaultvalue,
                0,
                10000
        );

    }) : null;

    public static final Holder<Attribute> MAX_SATURATION = Configs.foodSystemConfig.sectionSaturation.ENABLE_SATURATION_CHANGES
            ? ATTRIBUTES.register("max_saturation", () -> {

        double defaultvalue = Configs.foodSystemConfig.sectionSaturation.baseSaturation.get();

        return new RangedAttribute(
                // The translation key to use.
                descriptionIDPrefix + ".max_saturation",
                // The default value.
                defaultvalue,
                0,
                10000
        );

    }) : null;


    public static final Holder<Attribute> MAX_EXHAUSTION = Configs.foodSystemConfig.sectionExhaustion.ENABLE_EXHAUSTION_CHANGES
            ? ATTRIBUTES.register("max_exhaustion", () -> {

        double defaultvalue = Configs.foodSystemConfig.sectionExhaustion.baseExhaustion.get();

        return new RangedAttribute(
                // The translation key to use.
                descriptionIDPrefix + ".max_exhaustion",
                // The default value.
                defaultvalue,
                0,
                10000
        );

    }) : null;


    public static void register(IEventBus modEventBus) {
        ATTRIBUTES.register(modEventBus);
    }
}
