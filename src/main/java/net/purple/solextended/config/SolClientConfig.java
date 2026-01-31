package net.purple.solextended.config;

import me.fzzyhmstrs.fzzy_config.config.Config;
import net.purple.solextended.Constants;


//TODO Add all the Description, Names, etc for the client config here
public class SolClientConfig extends Config {

    public SolClientConfig() {
        super(Constants.rLSolClientConfig);
    }

    public Boolean shouldPlayMilestoneSound = true;
    public Boolean shouldSpawnMilestoneParticles = true;
    public Boolean shouldSpawnMaxMilestoneParticles = true;

    public Boolean isFoodTooltipEnabled = true;


}
