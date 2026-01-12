package net.purple.permfood;

import net.purple.permfood.communication.ModCommands;
import net.purple.permfood.config.Config;
import net.purple.permfood.config.ConfigAttributes;
import net.purple.permfood.moddata.ModData;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;

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


        //This way is needed, so that the statics are loaded
        ModData.register(modEventBus);

        // Commands
        NeoForge.EVENT_BUS.addListener(ModCommands::onCommandRegister);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC, MODID + ".toml");
        modContainer.registerConfig(ModConfig.Type.SERVER, ConfigAttributes.SPEC, MODID + "_attributes.toml");


    }

    // TODO solcarrot book integration

    //TODO Add real dependency > Dont run without

}
