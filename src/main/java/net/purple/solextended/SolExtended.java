package net.purple.solextended;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(SolExtended.MODID)
public class SolExtended {
    public static final String MODID = "solextended";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean IS_DEV = !FMLEnvironment.production;


}
