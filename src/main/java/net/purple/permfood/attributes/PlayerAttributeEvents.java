package net.purple.permfood.attributes;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.purple.permfood.config.Configs;
import net.purple.permfood.config.FoodSystemConfig;

import static net.purple.permfood.PermanentFood.MODID;

@EventBusSubscriber(modid = MODID)
public class PlayerAttributeEvents {

    @SubscribeEvent // This just initiates the attributes for players. Default values are set in ModAttributes.
    public static void modifyDefaultAttributes(EntityAttributeModificationEvent event) {

        FoodSystemConfig config = Configs.foodSystemConfig;

        if (config.sectionHunger.ENABLE_HUNGER_CHANGES) {
            event.add(EntityType.PLAYER, ModAttributes.MAX_HUNGER);
        }

        if (config.sectionSaturation.ENABLE_SATURATION_CHANGES) {
            event.add(EntityType.PLAYER, ModAttributes.MAX_SATURATION);
        }

        if (config.sectionExhaustion.ENABLE_EXHAUSTION_CHANGES) {
            event.add(EntityType.PLAYER, ModAttributes.MAX_EXHAUSTION);
        }
    }


    // Resets Base Values. Maybe needs to be checked later ?
    @SubscribeEvent
    public static void resetAttributes(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        player.getAttributes().assignBaseValues(player.getAttributes());
    }

}


