package net.purple.solextended.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static net.purple.solextended.SolExtended.MODID;

/**
 * Celebration payload: player entity ID and milestone flags for client-side celebration effects.
 */
public record MilestoneCelebrationPayload(
        int celebratingEntityId,
        boolean anyMilestoneReached,
        boolean anyMaxMilestoneReached
) implements CustomPacketPayload {

    public static final Type<MilestoneCelebrationPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "celebration"));

    public static final StreamCodec<FriendlyByteBuf, MilestoneCelebrationPayload> CODEC = StreamCodec.of(
            (buf, data) -> {
                buf.writeVarInt(data.celebratingEntityId());
                buf.writeBoolean(data.anyMilestoneReached());
                buf.writeBoolean(data.anyMaxMilestoneReached());
            },
            (buf) -> {
                int entityId = buf.readVarInt();
                boolean anyMilestone = buf.readBoolean();
                boolean anyMaxMilestone = buf.readBoolean();
                return new MilestoneCelebrationPayload(entityId, anyMilestone, anyMaxMilestone);
            }
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
