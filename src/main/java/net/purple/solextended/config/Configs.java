package net.purple.solextended.config;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;

public class Configs {

    public static SolExtendedConfig solExtendedConfig = ConfigApiJava.registerAndLoadConfig(SolExtendedConfig::new, RegisterType.BOTH);
    public static SolClientConfig solClientConfig = ConfigApiJava.registerAndLoadConfig(SolClientConfig::new, RegisterType.CLIENT);

    public static void init() {}

}
