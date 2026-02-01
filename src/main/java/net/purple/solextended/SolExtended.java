package net.purple.solextended;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.purple.solextended.api.milestonebased.MilestoneManagerRegistry;
import net.purple.solextended.config.Configs;
import net.purple.solextended.foodlist.PlayerFoodList;
import net.purple.solextended.item.SolExtendedItems;
import net.purple.solextended.networking.FoodListSyncHandler;
import org.slf4j.Logger;

import java.util.function.Supplier;

@Mod(SolExtended.MODID)
public class SolExtended {
    public static final String MODID = "solextended";
    // Directly reference a slf4j logger

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean IS_DEV = !FMLEnvironment.production;

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MODID);
    public static final Supplier<AttachmentType<PlayerFoodList>> FOOD_LIST_ATTACHMENT = ATTACHMENT_TYPES.register("food", () ->
            AttachmentType.serializable(PlayerFoodList::new).sync(new FoodListSyncHandler()).copyOnDeath().build());


    public SolExtended(IEventBus modEventBus) {

        Configs.init();
        SolExtendedItems.setUp(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);

        // Initialize milestone manager registry for all mods
        MilestoneManagerRegistry.register(modEventBus);
    }

    // TODO Cleanup "solcarrot" everywhere
    // TODO Write README.md and credit solcarrot
    // TODO ReWrite FoodBookScreen for a modern screen ???

    // TODO Test Peaceful Hunger not working

    // Empty Foodlist Page looks bad

    // TODO Tooltip
}
