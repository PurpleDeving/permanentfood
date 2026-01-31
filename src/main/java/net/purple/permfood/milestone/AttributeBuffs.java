package net.purple.permfood.milestone;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.purple.permfood.attributes.ModAttributes;
import net.purple.permfood.config.Configs;
import net.purple.permfood.config.FoodSystemConfig;
import net.purple.solextended.api.milestonebased.MilestoneManager;
import net.purple.solextended.foodlist.FoodListEvent;

import java.util.Set;

import static net.purple.permfood.PermanentFood.MODID;

@EventBusSubscriber(modid = MODID)
public class AttributeBuffs extends MilestoneManager<AttributeMilestoneProgression> {

    private static final AttributeMilestoneType MAX_HUNGER_BUFF = new AttributeMilestoneType("max_hunger_buff", Configs.foodSystemConfig.sectionHunger.milestonesHunger.get(), ModAttributes.MAX_HUNGER);
    private static final AttributeMilestoneType MAX_SATURATION_BUFF = new AttributeMilestoneType("max_saturation_buff", Configs.foodSystemConfig.sectionSaturation.milestonesSaturation.get(), ModAttributes.MAX_SATURATION);
    private static final AttributeMilestoneType MAX_EXHAUSTION_BUFF = new AttributeMilestoneType("max_exhaustion_buff", Configs.foodSystemConfig.sectionExhaustion.milestonesExhaustion.get(), ModAttributes.MAX_EXHAUSTION);

    public AttributeBuffs() {
        super();

        FoodSystemConfig foodSystemConfig = Configs.foodSystemConfig;

        this.getMilestoneProgressions().add(new AttributeMilestoneProgression(MAX_HUNGER_BUFF, foodSystemConfig.sectionHunger.perMilestoneHunger.get()));
        this.getMilestoneProgressions().add(new AttributeMilestoneProgression(MAX_SATURATION_BUFF, foodSystemConfig.sectionSaturation.perMilestoneSaturation.get()));
        this.getMilestoneProgressions().add(new AttributeMilestoneProgression(MAX_EXHAUSTION_BUFF, foodSystemConfig.sectionExhaustion.perMilestoneExhaustion.get()));

    }


    @SubscribeEvent
    public static void AtttributeBuffsFoodCountEventUpdate(FoodListEvent.PlayerFoodCountEvent event) {
        
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onFoodCoundUpdate(FoodListEvent.PlayerFoodCountEvent event) {
        Player player = event.getPlayer();
        AttributeMap playerAttributes = player.getAttributes();
        Set<AttributeMilestoneProgression> attributeBuffs = this.getMilestoneProgressions();
        for (AttributeMilestoneProgression attributeBuff : attributeBuffs) {
            AttributeMilestoneType attributeMilestoneType = (AttributeMilestoneType) attributeBuff.getType();
            Holder<Attribute> attribute = attributeMilestoneType.getAttribute();
            AttributeModifier attributeModifier = new AttributeModifier(attributeMilestoneType.getResourceLocation(), attributeBuff.getBuffValue(), AttributeModifier.Operation.ADD_VALUE);
            playerAttributes.getInstance(attribute).addOrReplacePermanentModifier(attributeModifier);
        }
    }
}
