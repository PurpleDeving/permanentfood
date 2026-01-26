package net.purple.permfood.moddata;

import com.cazsius.solcarrot.SOLCarrotConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.purple.permfood.moddata.attributes.PlayerAttributes;
import net.purple.permfood.moddata.baseClases.MilestoneDiff;
import net.purple.permfood.moddata.baseClases.PlayerFoodInstance;

import java.util.List;
import java.util.Map;

import static net.purple.permfood.PermanentFood.MODID;
import static net.purple.permfood.moddata.PlayerAttributeEvents.updatePlayerAttributes;
import static net.purple.permfood.moddata.attributes.PlayerAttributes.getOrCreatePlayerAttributes;

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

        Map<String, Integer> before = attrs.snapshotMilestonesReached();

        updatePlayerAttributes(player);

        Map<String, Integer> after = attrs.snapshotMilestonesReached();
        List<MilestoneDiff> diffs = PlayerFoodInstance.diffMilestones(before, after);

        if (!diffs.isEmpty()) {
            celebrateMilestones(player, diffs);
        }
    }

    private static void celebrateMilestones(Player player, List<MilestoneDiff> diffs) {
        // particles once
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.serverLevel().sendParticles(
                    ParticleTypes.HEART,
                    serverPlayer.getX(), serverPlayer.getY() + serverPlayer.getEyeHeight(), serverPlayer.getZ(),
                    12,
                    0.5D, 0.5D, 0.5D,
                    0.0D
            );
        }

        // sound once
        player.level().playSound(null,
                player.blockPosition(),
                SoundEvents.PLAYER_LEVELUP,
                SoundSource.PLAYERS,
                1.0F,
                1.0F);

        // messages: per stat per milestone crossed
        for (MilestoneDiff diff : diffs) {
            int delta = diff.delta();
            for (int i = 0; i < delta; i++) {
                player.sendSystemMessage(Component.literal("Milestone reached: ")
                        .append(Component.literal(diff.name()).withStyle(ChatFormatting.AQUA))
                        .append(Component.literal(" (" + (diff.oldReached() + i + 1) + ")")));
            }
        }
    }


}
