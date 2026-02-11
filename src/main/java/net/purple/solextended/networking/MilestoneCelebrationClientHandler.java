package net.purple.solextended.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.purple.solextended.config.Configs;

@OnlyIn(Dist.CLIENT)
public final class MilestoneCelebrationClientHandler {

    private MilestoneCelebrationClientHandler() {}

    public static void handle(MilestoneCelebrationPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            if (level == null) {
                return;
            }

            Entity entity = level.getEntity(payload.celebratingEntityId());
            if (!(entity instanceof Player celebratingPlayer)) {
                return;
            }


            if (payload.anyMaxMilestoneReached()) {

                if (Configs.solClientConfig.shouldSpawnMaxMilestoneParticles) {
                    spawnParticles(level, celebratingPlayer, ParticleTypes.HAPPY_VILLAGER, 16);
                } else if (Configs.solClientConfig.shouldSpawnMilestoneParticles) {
                    spawnParticles(level, celebratingPlayer, ParticleTypes.HEART, 16);
                }

                if (Configs.solClientConfig.shouldPlayMilestoneSound) {
                    playSound(level, celebratingPlayer, SoundEvents.PLAYER_LEVELUP, 1.0f);
                }


            } else if (payload.anyMilestoneReached()) {

                if (Configs.solClientConfig.shouldSpawnMilestoneParticles) {
                    spawnParticles(level, celebratingPlayer, ParticleTypes.HEART, 12);
                }

                if (Configs.solClientConfig.shouldPlayMilestoneSound) {
                    playSound(level, celebratingPlayer, SoundEvents.PLAYER_LEVELUP, 0.6f);
                }

            } else {

                if (Configs.solClientConfig.shouldSpawnParticlesForNewFood && celebratingPlayer == mc.player) {
                    spawnParticles(level, celebratingPlayer, ParticleTypes.END_ROD, 12);
                }

            }
        });
    }


    private static void spawnParticles(ClientLevel level, Player player, ParticleOptions particle, int count) {
        double x = player.getX();
        double y = player.getY() + player.getBbHeight() * 0.6;
        double z = player.getZ();

        for (int i = 0; i < count; i++) {
            double dx = (level.random.nextDouble() - 0.5) * 0.6;
            double dy = (level.random.nextDouble()) * 0.6;
            double dz = (level.random.nextDouble() - 0.5) * 0.6;
            level.addParticle(particle, x + dx, y + dy, z + dz, 0.0, 0.02, 0.0);
        }
    }

    private static void playSound(ClientLevel level, Player player, net.minecraft.sounds.SoundEvent sound, float volume) {
        level.playLocalSound(player.getX(), player.getY(), player.getZ(), sound, net.minecraft.sounds.SoundSource.PLAYERS, volume, 1.0f, false);
    }
}
