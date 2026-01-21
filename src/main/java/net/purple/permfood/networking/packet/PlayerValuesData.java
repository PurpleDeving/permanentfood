package net.purple.permfood.networking.packet;


/*public record PlayerValuesData(PlayerAttributes newValues) implements CustomPacketPayload {

    public static final Type<PlayerValuesData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(PermanentFood.MODID, "player_values_data"));

*//*    public static final StreamCodec<ByteBuf, PlayerValuesData> STREAM_CODEC = StreamCodec.of(
            (buf, pvData) -> pvData.newValues().writeToBuf(buf),
            (buf) -> new PlayerValuesData((PlayerAttributes) PlayerAttributes.readFromBuf(buf, PlayerAttributes.class, player))
    ); TODO - Evalute if needed*//*


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}*/
