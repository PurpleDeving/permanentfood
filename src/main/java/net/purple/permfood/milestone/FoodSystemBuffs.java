package net.purple.permfood.milestone;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.purple.permfood.attributes.ModAttributes;
import net.purple.permfood.config.Configs;
import net.purple.permfood.config.FoodSystemConfig;
import net.purple.solextended.api.milestonebased.MilestoneManager;
import net.purple.solextended.api.milestonebased.MilestoneManagerRegistry;
import org.jline.utils.Log;

import java.util.List;
import java.util.function.Supplier;

import static net.purple.permfood.PermanentFood.MODID;

/**
 * Manages attribute buffs for players based on milestone progression.
 * Each player has their own instance of this class via DataAttachment managed by solextended.
 */
public class FoodSystemBuffs extends MilestoneManager<AttributeMilestoneProgression> {

    private static final AttributeMilestoneType MAX_HUNGER_BUFF = new AttributeMilestoneType("max_hunger_buff",
            Configs.foodSystemConfig.sectionHunger.milestonesHunger.get(),
            ModAttributes.MAX_HUNGER,
            "Hunger",
            Configs.foodSystemConfig.sectionHunger.ENABLE_HUNGER_CHANGES);

    private static final AttributeMilestoneType MAX_SATURATION_BUFF = new AttributeMilestoneType("max_saturation_buff",
            Configs.foodSystemConfig.sectionSaturation.milestonesSaturation.get(),
            ModAttributes.MAX_SATURATION,
            "Saturation",
            Configs.foodSystemConfig.sectionSaturation.ENABLE_SATURATION_CHANGES);

    private static final AttributeMilestoneType MAX_EXHAUSTION_BUFF = new AttributeMilestoneType("max_exhaustion_buff",
            Configs.foodSystemConfig.sectionExhaustion.milestonesExhaustion.get(),
            ModAttributes.MAX_EXHAUSTION,
            "Exhaustion",
            Configs.foodSystemConfig.sectionExhaustion.ENABLE_EXHAUSTION_CHANGES);

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
        List<AttributeMilestoneProgression> attributeBuffs = this.getMilestoneProgressions();

        for (AttributeMilestoneProgression attributeBuff : attributeBuffs) {
            // Update milestone progression
            attributeBuff.updateMilestonesReached(foodCount);

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

        for (AttributeMilestoneProgression progression : this.getMilestoneProgressions()) {
            AttributeMilestoneType type = (AttributeMilestoneType) progression.getType();
            String declareName = type.declareName;

            output.append(Component.translatable(
                            "command." + MODID + ".milestone.attribute_buffs.section_header",
                            declareName)
                    .withStyle(ChatFormatting.YELLOW));
            output.append("\n");

            // Enabled/disabled label is localized separately so languages can vary word order.
            Component enabledLabel = Component.translatable(
                    "command." + MODID + ".milestone.attribute_buffs." + (type.isEnabled ? "enabled" : "disabled"));

            output.append(Component.translatable(
                            "command." + MODID + ".milestone.attribute_buffs.changes",
                            declareName,
                            enabledLabel)
                    .withStyle(ChatFormatting.WHITE));
            output.append("\n");

            if (!type.isEnabled) {
                continue;
            }

            double current = AttributeMilestoneProgression.round(player.getAttributeValue(type.getAttribute()), 1);
            double bonus = AttributeMilestoneProgression.round(progression.getBuffValue(), 1);

            output.append(Component.translatable(
                            "command." + MODID + ".milestone.attribute_buffs.current_and_bonus",
                            declareName,
                            current,
                            bonus)
                    .withStyle(ChatFormatting.WHITE));
            output.append("\n");
        }
    }


}
