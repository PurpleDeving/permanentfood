package net.purple.solextended.networking;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static net.purple.solextended.SolExtended.MODID;

public record FoodListData(CompoundTag foodListNBT) implements CustomPacketPayload {

    public static final Type<FoodListData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "food_list_data"));

    public static final StreamCodec<FriendlyByteBuf, FoodListData> CODEC = StreamCodec.of(
            (buf, data) -> buf.writeNbt(data.foodListNBT()),
            (buf) -> new FoodListData(buf.readNbt())
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
