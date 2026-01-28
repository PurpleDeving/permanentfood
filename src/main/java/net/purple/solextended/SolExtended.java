package net.purple.solextended;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.purple.solextended.config.Configs;
import org.slf4j.Logger;

@Mod(SolExtended.MODID)
public class SolExtended {
    public static final String MODID = "solextended";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean IS_DEV = !FMLEnvironment.production;


    public SolExtended(IEventBus modEventBus) {

        Configs.init();
    }


}
