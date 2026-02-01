package net.purple.permfood.milestone;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.purple.permfood.Constants;
import net.purple.permfood.attributes.ModAttributes;
import net.purple.permfood.config.Configs;
import net.purple.permfood.config.FoodSystemConfig;
import net.purple.solextended.api.milestonebased.MilestoneManager;
import net.purple.solextended.api.milestonebased.MilestoneManagerRegistry;
import org.jline.utils.Log;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static net.purple.permfood.PermanentFood.MODID;

/**
 * Manages attribute buffs for players based on milestone progression.
 * Each player has their own instance of this class via DataAttachment managed by solextended.
 */
public class FoodSystemBuffs extends MilestoneManager<AttributeMilestoneProgression> {

    private static final AttributeMilestoneType MAX_HUNGER_BUFF = new AttributeMilestoneType("max_hunger_buff", Configs.foodSystemConfig.sectionHunger.milestonesHunger.get(), ModAttributes.MAX_HUNGER);
    private static final AttributeMilestoneType MAX_SATURATION_BUFF = new AttributeMilestoneType("max_saturation_buff", Configs.foodSystemConfig.sectionSaturation.milestonesSaturation.get(), ModAttributes.MAX_SATURATION);
    private static final AttributeMilestoneType MAX_EXHAUSTION_BUFF = new AttributeMilestoneType("max_exhaustion_buff", Configs.foodSystemConfig.sectionExhaustion.milestonesExhaustion.get(), ModAttributes.MAX_EXHAUSTION);

    private static Supplier<AttachmentType<FoodSystemBuffs>> ATTACHMENT_TYPE;

    /**
     * Registers this manager type with the central registry.
     *
     * <p>This must run before NeoForge's attachment type {@code RegisterEvent} fires.
     * Do <b>not</b> call this from common setup.</p>
     */
    public static void init() {
        if (ATTACHMENT_TYPE != null) {
            return;
        }
        ATTACHMENT_TYPE = MilestoneManagerRegistry.registerManager(MODID, "attribute_buffs", FoodSystemBuffs::new);
        Log.info("Registered AttributeBuffs milestone manager");
    }

    public FoodSystemBuffs() {
        super();

        FoodSystemConfig foodSystemConfig = Configs.foodSystemConfig;

        this.getMilestoneProgressions().add(new AttributeMilestoneProgression(MAX_HUNGER_BUFF, foodSystemConfig.sectionHunger.perMilestoneHunger.get()));
        this.getMilestoneProgressions().add(new AttributeMilestoneProgression(MAX_SATURATION_BUFF, foodSystemConfig.sectionSaturation.perMilestoneSaturation.get()));
        this.getMilestoneProgressions().add(new AttributeMilestoneProgression(MAX_EXHAUSTION_BUFF, foodSystemConfig.sectionExhaustion.perMilestoneExhaustion.get()));
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onFoodCountUpdate(Player player, int foodCount) {
        Log.warn("Updating AttributeBuffs for player: " + player.getName().getString() + " with foodCount: " + foodCount);
        AttributeMap playerAttributes = player.getAttributes();
        Set<AttributeMilestoneProgression> attributeBuffs = this.getMilestoneProgressions();

        for (AttributeMilestoneProgression attributeBuff : attributeBuffs) {
            // Update milestone progression
            attributeBuff.checkReachedMilestoneUpdate(foodCount);

            // Apply or update the attribute modifier
            AttributeMilestoneType attributeMilestoneType = (AttributeMilestoneType) attributeBuff.getType();
            Holder<Attribute> attribute = attributeMilestoneType.getAttribute();
            AttributeModifier attributeModifier = new AttributeModifier(
                    attributeMilestoneType.getResourceLocation(),
                    attributeBuff.getBuffValue(),
                    AttributeModifier.Operation.ADD_VALUE
            );
            playerAttributes.getInstance(attribute).addOrReplacePermanentModifier(attributeModifier);
        }
    }

    @Override
    public void outputStats(ServerPlayer player, int foodCount, MutableComponent output) {
        FoodSystemConfig.HungerSection hunger = Configs.foodSystemConfig.sectionHunger;

        output.append("§e--- Food System: Hunger ---\n");
        output.append("§7Hunger Changes: §f" + (hunger.ENABLE_HUNGER_CHANGES ? "Enabled" : "Disabled") + "\n");

        if (!hunger.ENABLE_HUNGER_CHANGES) {
            output.append("§7Max Hunger: §f" + Constants.VANILLA_MAX_HUNGER + "\n");
            return;
        }

        // Determine milestones reached for max hunger.
        Optional<AttributeMilestoneProgression> maxHungerProg = this.getMilestoneProgressions().stream()
                .filter(p -> p.getType() == MAX_HUNGER_BUFF)
                .findFirst();

        int reached = maxHungerProg.map(AttributeMilestoneProgression::getMilestonesReached).orElse(0);
        int bonus = (int) (reached * hunger.perMilestoneHunger.get());

        // Current max hunger value as applied to the player.
        double current = player.getAttributeValue(ModAttributes.MAX_HUNGER);

        output.append("§7Max Hunger is §f" + (int) current + "§7 with a Milestone Bonus of §f" + bonus + ".\n");
    }
}
