package net.purple.permfood.config;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;

public class Configs {

    public static FoodSystemConfig fooodSystemConfig = ConfigApiJava.registerAndLoadConfig(FoodSystemConfig::new, RegisterType.BOTH);

    public static void init() {}

}
