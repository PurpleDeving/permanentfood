package net.purple.solextended.config;

import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.util.Translatable;
import net.purple.solextended.Constants;

import static net.purple.solextended.SolExtended.MODID;

@Translatable.Name("Sol Client Config")
@Translation(prefix = MODID + ".sol_client_config")
public class SolClientConfig extends Config {

    public SolClientConfig() {
        super(Constants.rLSolClientConfig);
    }

    // TODO Implementation missng

    @Name("Milestone Sound")
    @Desc("Whether a sounds should play when a milestone is reached.")
    public Boolean shouldPlayMilestoneSound = true;
    @Name("Milestone Particles")
    @Desc("Whether particles spawn when a milestone is reached.")
    public Boolean shouldSpawnMilestoneParticles = true;
    @Name("Max Milestone Particles")
    @Desc("Whether additional particles spawn when a maximum milestone is reached.")
    public Boolean shouldSpawnMaxMilestoneParticles = true;

    @Name("New Food Particles")
    @Desc("Whether particles should spawn when new food is eaten for the first time.")
    public Boolean shouldPlayParticlesForNewFood = true; //IMPL missing

    @Name("Food Tooltips")
    @Desc("Whether food tooltips for eaten/not eaten foods is shown.")
    public Boolean isFoodTooltipEnabled = true;


}
