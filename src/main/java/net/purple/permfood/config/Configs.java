package net.purple.permfood.config;

import me.fzzyhmstrs.fzzy_config.api.ConfigApi;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;

public class Configs {

    public static HungerConfig hungerConfig = ConfigApiJava.registerAndLoadConfig(HungerConfig::new, RegisterType.BOTH);

    public static void init() {}

}
