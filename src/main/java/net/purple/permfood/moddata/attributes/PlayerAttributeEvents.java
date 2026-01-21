package net.purple.permfood.moddata.attributes;

import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.purple.permfood.config.ConfigAttributes;

import static net.purple.permfood.PermanentFood.MODID;

@EventBusSubscriber(modid = MODID)
public class PlayerAttributeEvents {

    @SubscribeEvent // From ModAttributes overwrite the defaults when needed.
    public static void modifyDefaultAttributes(EntityAttributeModificationEvent event) {


        if (ConfigAttributes.ENABLE_MAX_HUNGER_CHANGES.get()) {
            event.add(EntityType.PLAYER, ModAttributes.MAX_HUNGER, ConfigAttributes.NEW_BASE_MAX_HUNGER.get());
        }
/*
        if (ConfigAttributes.ENABLE_MAX_SATURATION_CHANGES.get()) {
            event.add(EntityType.PLAYER, ModAttributes.MAX_HUNGER, ConfigAttributes.NEW_BASE_MAX_SATURATION.get());
        }

        if (ConfigAttributes.ENABLE_MAX_EXHAUSTION_CHANGES.get()) {
            event.add(EntityType.PLAYER, ModAttributes.MAX_HUNGER, ConfigAttributes.NEW_BASE_MAX_EXHAUSTION.get());
        }*/

    }
}
