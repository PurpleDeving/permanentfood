package net.purple.solextended.networking;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.purple.solextended.foodlist.PlayerFoodList;
import org.jetbrains.annotations.Nullable;

public class FoodListSyncHandler implements AttachmentSyncHandler<PlayerFoodList> {

    @Override
    public void write(RegistryFriendlyByteBuf buf, PlayerFoodList foodList, boolean initialSync) {
        // Serialize to the same stable format used by the explicit payload.
        CompoundTag tag = foodList.serializeNBT(buf.registryAccess());
        FoodListData.CODEC.encode(buf, new FoodListData(tag));
    }

    @Override
    public @Nullable PlayerFoodList read(@Nullable IAttachmentHolder holder, @Nullable RegistryFriendlyByteBuf buf, @Nullable PlayerFoodList existing) {
        if (buf == null) {
            return existing;
        }

        FoodListData decoded = FoodListData.CODEC.decode(buf);

        PlayerFoodList result = (existing != null) ? existing : new PlayerFoodList();
        // Apply on the receiving side (client): decode NBT using the holder/buffer registry access.
        result.deserializeNBT(buf.registryAccess(), decoded.foodListNBT());
        return result;
    }
}
