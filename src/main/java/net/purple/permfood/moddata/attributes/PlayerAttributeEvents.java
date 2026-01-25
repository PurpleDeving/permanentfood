package net.purple.permfood.moddata.attributes;

import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.purple.permfood.config.ConfigAttributes;
import net.purple.permfood.config.ConfigStartup;
import net.purple.permfood.config.HungerConfig;

import static net.purple.permfood.PermanentFood.MODID;
import static net.purple.permfood.moddata.attributes.ModAttributes.MAX_HUNGER;

@EventBusSubscriber(modid = MODID)
public class PlayerAttributeEvents {

    @SubscribeEvent // From ModAttributes overwrite the defaults when needed.
    public static void modifyDefaultAttributes(EntityAttributeModificationEvent event) {

        HungerConfig.HungerSection hungerSection = new HungerConfig.HungerSection();

        if (hungerSection.ENABLE_HUNGER_CHANGES) {
            event.add(EntityType.PLAYER, MAX_HUNGER, hungerSection.baseHunger);
        }
/*
        if (ConfigStartup.ENABLE_MAX_SATURATION_CHANGES.get()) {
            event.add(EntityType.PLAYER, ModAttributes.MAX_HUNGER, ConfigAttributes.NEW_BASE_MAX_SATURATION.get());
        }

        if (ConfigStartup.ENABLE_MAX_EXHAUSTION_CHANGES.get()) {
            event.add(EntityType.PLAYER, ModAttributes.MAX_HUNGER, ConfigAttributes.NEW_BASE_MAX_EXHAUSTION.get());
        }*/

    }


}
