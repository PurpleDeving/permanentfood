package net.purple.solextended.config;

import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import net.purple.solextended.Constants;

import static net.purple.solextended.SolExtended.MODID;

@Translation(prefix = MODID + ".sol_client_config.")
public class SolClientConfig extends Config {

    public SolClientConfig() {
        super(Constants.rLSolClientConfig);
    }

    // TODO Implementation missng

    @Desc("Whether a sounds should play when a milestone is reached.")
    public Boolean shouldPlayMilestoneSound = true;
    @Desc("Whether particles sounds spawn when a milestone is reached.")
    public Boolean shouldSpawnMilestoneParticles = true;
    @Desc("Whether additional particles sounds spawn when a maximum milestone is reached.")
    public Boolean shouldSpawnMaxMilestoneParticles = true;

    @Desc("Whether a sound should play when new food is eaten for the first time.")
    public Boolean shouldPlayParticlesForNewFood = true; //IMPL missing

    @Desc("Whether food tooltips for eaten/not eaten foods is shown.")
    public Boolean isFoodTooltipEnabled = true;


}
