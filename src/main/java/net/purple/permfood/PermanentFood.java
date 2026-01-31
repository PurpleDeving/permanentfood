package net.purple.permfood;


import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.purple.permfood.attributes.ModAttributes;
import net.purple.permfood.config.Configs;
import org.slf4j.Logger;

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

        Configs.init();

        ModAttributes.register(modEventBus);


        // Commands
        //TODO NeoForge.EVENT_BUS.addListener(ModCommands::onCommandRegister);


    }


    // TODO


    // TODO solcarrot book integration

    // TODO Add Milestone Reached Message

}
