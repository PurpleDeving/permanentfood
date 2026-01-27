/*
package backup.moddata;

import backup.moddata.attributes.PlayerAttribute;
import backup.moddata.attributes.PlayerAttributes;
import com.cazsius.solcarrot.SOLCarrotConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

import java.util.ArrayList;
import java.util.List;

import static backup.moddata.PlayerAttributeEvents.updatePlayerAttributes;
import static backup.moddata.attributes.PlayerAttributes.getOrCreatePlayerAttributes;
import static net.purple.permfood.PermanentFood.MODID;

@EventBusSubscriber(modid = MODID)
public final class FoodSystemEvents {


    //Booth Sides
    @SubscribeEvent(priority = EventPriority.LOW) // Guaranteed to trigger after solcarrot
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (SOLCarrotConfig.limitProgressionToSurvival() && player.isCreative()) return;

        var usedStack = event.getItem();
        if (usedStack.getFoodProperties(player) == null) return;

        // Server is source of truth: apply modifiers (auto-sync) and decide milestone celebration.
        if (EffectiveSide.get().isServer()) {
            handleFoodEatenServer(player);
        }
    }


    private static void handleFoodEatenServer(Player player) {
        PlayerAttributes attrs = getOrCreatePlayerAttributes(player);

        // apply config + update milestone counters + apply modifiers
        updatePlayerAttributes(player);


        celebrateMilestones(player, attrs);

    }


    private static void celebrateMilestones(Player player, PlayerAttributes attrs) {
        List<PlayerAttribute> reachedMilestonesAttributes = new ArrayList<>();
        boolean anyReachedMaximum = false;
        for (PlayerAttribute attribute : attrs.getMilestoneBasedList()) {
            // TODO - Resseting the previous milestone is missing. Currently triggers on all food once a milestone is reached after a restart.
            if (attribute.advancedMilestone()) {
                reachedMilestonesAttributes.add(attribute);
            }
        }
        ServerPlayer celebratingPlayer = (ServerPlayer) player;

        for (PlayerAttribute attribute : reachedMilestonesAttributes) {
            Boolean isMaximum = false;
            if (attribute.maxMilestonesReached()) {
                anyReachedMaximum = true;
                isMaximum = true;
            }

            celebrateMessage(celebratingPlayer, attribute, isMaximum);
        }

        celebrateSound(celebratingPlayer);
        celebrateParticles(anyReachedMaximum, celebratingPlayer);
    }


    private static void celebrateMessage(ServerPlayer celebratingPlayer, PlayerAttribute attribute, Boolean isMaximum) {
        String bonus = String.format(java.util.Locale.ROOT, "%.2f", attribute.getAddedValue());

        //TODO - Need a better way to do localization here + Naming for the attributes is messed up.
        MutableComponent msg;
        if (isMaximum) {
            msg = Component.literal("Maximum milestone reached for ").append(Component.literal(attribute.getName()).withStyle(ChatFormatting.AQUA) + ".\n")
                    .append(Component.literal("The maximum bonus is: ").withStyle(ChatFormatting.GOLD)).append(Component.literal(bonus + "."));
        } else {
            msg = Component.literal("Milestone reached for ").append(Component.literal(attribute.getName()).withStyle(ChatFormatting.AQUA) + ".\n")
                    .append(Component.literal("Current bonus: ").withStyle(ChatFormatting.GREEN)).append(Component.literal(bonus + "."));
        }
        celebratingPlayer.sendSystemMessage(msg);
    }

    private static void celebrateSound(ServerPlayer serverPlayer) {
        serverPlayer.level().playSound(null,
                serverPlayer.blockPosition(),
                SoundEvents.PLAYER_LEVELUP,
                SoundSource.PLAYERS,
                1.0F,
                1.0F);
    }

    private static void celebrateParticles(Boolean reachedMaximum, ServerPlayer serverPlayer) {
        serverPlayer.serverLevel().sendParticles(
                ParticleTypes.HEART,
                serverPlayer.getX(), serverPlayer.getY() + serverPlayer.getEyeHeight(), serverPlayer.getZ(),
                12,
                0.5D, 0.5D, 0.5D,
                0.0D
        );

        if (reachedMaximum) {
            serverPlayer.serverLevel().sendParticles(
                    ParticleTypes.HAPPY_VILLAGER,
                    serverPlayer.getX(), serverPlayer.getY() + serverPlayer.getEyeHeight(), serverPlayer.getZ(),
                    16,
                    0.5D, 0.5D, 0.5D,
                    0.0D
            );
        }
    }

}
*/
