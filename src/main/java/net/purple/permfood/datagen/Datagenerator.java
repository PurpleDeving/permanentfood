package net.purple.permfood.datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static net.purple.permfood.PermanentFood.MODID;

@EventBusSubscriber(modid = MODID)
public class Datagenerator {


    @SubscribeEvent // on the mod event bus
    public static void onGatherData(GatherDataEvent event) {
        // Call event.createDatapackRegistryObjects(...) first if adding datapack objects

        event.createProvider(context -> new ConfigLanguageProvider(context, "de_de"));
        event.createProvider(context -> new ConfigLanguageProvider(context, "en_us"));

    }

}
