package net.purple.permfood.Networking.packet;


import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.purple.permfood.PermanentFood;
import net.purple.permfood.moddata.PlayerValues;

public record PlayerValuesData(PlayerValues newValues) implements CustomPacketPayload {

    public static final Type<PlayerValuesData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(PermanentFood.MODID, "player_values_data"));

    public static final StreamCodec<ByteBuf, PlayerValuesData> STREAM_CODEC = StreamCodec.of(
            (buf, pvData) -> pvData.newValues().writeToBuf(buf),
            (buf) -> new PlayerValuesData(PlayerValues.readFromBuf(buf))
    );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
