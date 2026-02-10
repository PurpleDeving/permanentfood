package net.purple.solextended.networking;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static net.purple.solextended.SolExtended.MODID;

/**
 * Generic celebration payload: a target entity id + a particle, count, optional sound, and volume.
 */
public record MilestoneCelebrationPayload(
        int celebratingEntityId,
        ResourceLocation particleId,
        int particleCount,
        ResourceLocation soundId,
        float volume
) implements CustomPacketPayload {

    public static final Type<MilestoneCelebrationPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "celebration"));

    public static final StreamCodec<FriendlyByteBuf, MilestoneCelebrationPayload> CODEC = StreamCodec.of(
            (buf, data) -> {
                buf.writeVarInt(data.celebratingEntityId());
                buf.writeResourceLocation(data.particleId());
                buf.writeVarInt(data.particleCount());

                boolean hasSound = data.soundId() != null;
                buf.writeBoolean(hasSound);
                if (hasSound) {
                    buf.writeResourceLocation(data.soundId());
                    buf.writeFloat(data.volume());
                }
            },
            (buf) -> {
                int entityId = buf.readVarInt();
                ResourceLocation particleId = buf.readResourceLocation();
                int particleCount = buf.readVarInt();

                ResourceLocation soundId = null;
                float volume = 0.0f;
                if (buf.readBoolean()) {
                    soundId = buf.readResourceLocation();
                    volume = buf.readFloat();
                }

                return new MilestoneCelebrationPayload(entityId, particleId, particleCount, soundId, volume);
            }
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public boolean hasSound() {
        return soundId != null && BuiltInRegistries.SOUND_EVENT.containsKey(soundId);
    }
}
