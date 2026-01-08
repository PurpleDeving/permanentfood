package net.purple.permfood;

import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.*;
import net.purple.permfood.config.Config;
import net.purple.permfood.moddata.ModData;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(PermanentFood.MODID)
public class PermanentFood {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "permanentfood";
    // Directly reference a slf4j logger
    public static final Logger LOG = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public PermanentFood(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        //TODO Does this work or need Kaupens Way with the method in ModData?
        ModData.register(modEventBus);

        // Commands
        NeoForge.EVENT_BUS.addListener(ModCommands::onCommandRegister);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC, MODID + ".toml");


    }


    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOG.info("HELLO FROM COMMON SETUP");

    }


    //TODO Add real dependency > Dont run without

}
