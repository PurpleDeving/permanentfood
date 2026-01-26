package net.purple.permfood.moddata.attributes;

import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.purple.permfood.config.Configs;
import net.purple.permfood.config.FoodSystemConfig;

import static net.purple.permfood.PermanentFood.MODID;
import static net.purple.permfood.moddata.attributes.ModAttributes.MAX_HUNGER;

@EventBusSubscriber(modid = MODID)
public class PlayerAttributeEvents {

    @SubscribeEvent // From ModAttributes overwrite the defaults when needed.
    public static void modifyDefaultAttributes(EntityAttributeModificationEvent event) {

        FoodSystemConfig config = Configs.foodSystemConfig;

        if (config.sectionHunger.ENABLE_HUNGER_CHANGES) {
            event.add(EntityType.PLAYER, MAX_HUNGER, config.sectionHunger.baseHunger.get());
        }

        if (config.sectionSaturation.ENABLE_SATURATION_CHANGES) {
            event.add(EntityType.PLAYER, ModAttributes.MAX_SATURATION, config.sectionSaturation.baseSaturation.get());
        }

        if (config.sectionExhaustion.ENABLE_EXHAUSTION_CHANGES) {
            event.add(EntityType.PLAYER, ModAttributes.MAX_EXHAUSTION, config.sectionExhaustion.baseExhaustion.get());
        }

    }


}
