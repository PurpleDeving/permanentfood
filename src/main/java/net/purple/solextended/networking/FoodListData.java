package net.purple.solextended.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static net.purple.solextended.SolExtended.MODID;

public record FoodListData(Set<Item> eatenfoods) implements CustomPacketPayload {


    public static final Type<FoodListData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "food_list_data"));


    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
