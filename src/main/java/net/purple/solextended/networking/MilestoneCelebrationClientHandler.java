package net.purple.solextended.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
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

            // Per-viewer config gating
            boolean playSoundAllowed = Boolean.TRUE.equals(Configs.solClientConfig.shouldPlayMilestoneSound);
            boolean particlesAllowed = Boolean.TRUE.equals(Configs.solClientConfig.shouldSpawnMilestoneParticles);

            if (particlesAllowed) {
                ParticleOptions particle = resolveParticle(payload.particleId());
                if (particle != null && payload.particleCount() > 0) {
                    spawnParticles(level, celebratingPlayer, particle, payload.particleCount());
                }
            }

            if (playSoundAllowed && payload.hasSound() && payload.volume() > 0.0f) {
                SoundEvent sound = resolveSound(payload.soundId());
                if (sound != null) {
                    level.playLocalSound(
                            celebratingPlayer.getX(), celebratingPlayer.getY(), celebratingPlayer.getZ(),
                            sound,
                            SoundSource.PLAYERS,
                            payload.volume(),
                            1.0f,
                            false
                    );
                }
            }
        });
    }

    private static ParticleOptions resolveParticle(ResourceLocation id) {
        if (id == null || !BuiltInRegistries.PARTICLE_TYPE.containsKey(id)) {
            return null;
        }

        ParticleType<?> type = BuiltInRegistries.PARTICLE_TYPE.get(id);
        // Minimal: only support simple particle types (vanilla's HAPPY_VILLAGER, HEART, END_ROD, etc.)
        if (type instanceof SimpleParticleType simple) {
            return simple;
        }

        return null;
    }

    private static SoundEvent resolveSound(ResourceLocation id) {
        if (id == null || !BuiltInRegistries.SOUND_EVENT.containsKey(id)) {
            return null;
        }
        return BuiltInRegistries.SOUND_EVENT.get(id);
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
}
