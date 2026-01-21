package net.purple.permfood.moddata;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.purple.permfood.moddata.attributes.PlayerAttributes;

import java.util.function.Supplier;

import static net.purple.permfood.PermanentFood.MODID;

public class ModData {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    public static final Supplier<AttachmentType<PlayerAttributes>> PLAYER_ATTRIBUTES = ATTACHMENT_TYPES.register("player_attributes_new",
            () -> AttachmentType.builder(() -> new PlayerAttributes(null, 0))
                    .build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }

}
