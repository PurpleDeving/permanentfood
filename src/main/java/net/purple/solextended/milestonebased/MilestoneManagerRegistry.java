package net.purple.solextended.milestonebased;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.purple.solextended.Constants;
import net.purple.solextended.SolExtended;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static net.purple.solextended.SolExtended.FOOD_LIST_ATTACHMENT;
import static net.purple.solextended.SolExtended.IS_DEV;

/**
 * Central registry for all milestone managers across all mods.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Mods register manager types via {@link #registerManager(String, String, Supplier)}.</li>
 *   <li>solextended stores per-player instances using DataAttachments.</li>
 *   <li>Call {@link #updateAllManagersForPlayer(Player, int)} to refresh all registered managers for one player.</li>
 * </ul>
 */
public class MilestoneManagerRegistry {

    /******************************************
     Handling for this Registry itself.
     ******************************************/

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, SolExtended.MODID);

    /**
     * Stores all registered manager factories by their resource location.
     * This allows iteration over all registered types for bulk operations.
     */
    private static final Map<ResourceLocation, ManagerRegistration<?>> REGISTERED_MANAGERS = new LinkedHashMap<>();

    /**
     * Flag to ensure registry registration is only initialized once.
     */
    private static boolean registered = false;


    private record ManagerRegistration<M extends MilestoneManager<?>>(
            ResourceLocation id,
            Supplier<AttachmentType<M>> attachmentType) {
    }

    /**
     * Registers the underlying attachment types with NeoForge.
     * Must be called during solextended mod initialization.
     */
    public static void register(IEventBus modEventBus) {
        if (!registered) {
            ATTACHMENT_TYPES.register(modEventBus);
            registered = true;
        }
    }

    /******************************************
     Registry for implementing mods to register their managers.
     ******************************************/


    /**
     * Registers a milestone manager type that will be available to all players.
     * Each player will get their own instance of this manager.
     */
    public static <M extends MilestoneManager<?>> Supplier<AttachmentType<M>> registerManager(
            String modid,
            String name,
            Supplier<M> factory
    ) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(modid, name);

        if (REGISTERED_MANAGERS.containsKey(id)) {
            throw new IllegalStateException("Milestone manager already registered: " + id);
        }

        // Prefix to reduce chance of name collision in solextended's attachment namespace.
        Supplier<AttachmentType<M>> attachmentType = ATTACHMENT_TYPES.register(
                modid + "_" + name,
                () -> AttachmentType.builder(factory).build()
        );

        REGISTERED_MANAGERS.put(id, new ManagerRegistration<>(id, attachmentType));

        SolExtended.LOGGER.info("Registered milestone manager: {} ({})", id, factory.get().getClass().getSimpleName());

        return attachmentType;
    }

    /**
     * Retrieves a specific manager instance for a player.
     */
    public static <M extends MilestoneManager<?>> M getManagerForPlayer(Player player, Supplier<AttachmentType<M>> attachmentType) {
        return player.getData(attachmentType);
    }


    /******************************************
     Show stats output for chat commands etc.
     ******************************************/

    public static void outputStatsAllManagers(ServerPlayer player, int foodCount, MutableComponent output) {
        for (ManagerRegistration<?> registration : REGISTERED_MANAGERS.values()) {
            outputStatsManager(player, foodCount, output, registration);
        }
    }

    private static <M extends MilestoneManager<?>> void outputStatsManager(ServerPlayer player, int foodCount, MutableComponent output, ManagerRegistration<M> registration) {
        try {
            M manager = player.getData(registration.attachmentType);
            manager.outputStats(player, foodCount, output);
        } catch (Exception e) {
            SolExtended.LOGGER.error(
                    "Failed to output stats for milestone manager {} for player {}",
                    registration.id,
                    player.getName().getString(),
                    e
            );
        }
    }

    /******************************************
     Collect Milestone Updates for Client-bound celebration
     ******************************************/

    public static MilestoneMessage collectMilestoneMessagesForPlayer(Player player) {
        MilestoneMessage milestoneMessages = new MilestoneMessage();

        for (ManagerRegistration<?> registration : REGISTERED_MANAGERS.values()) {
            player.getData(registration.attachmentType).gatherMilestoneMessages(player, milestoneMessages);
        }

        return milestoneMessages;
    }


    /******************************************
     Updates all milestone managers for all online players.
     Useful for config reloads or server-wide updates.
     ******************************************/

    public static void updateAllManagersForAllPlayers() {

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }
        List<ServerPlayer> players = server.getPlayerList().getPlayers();

        for (ServerPlayer player : players) {
            net.purple.solextended.foodlist.PlayerFoodList foodList =
                    player.getData(SolExtended.FOOD_LIST_ATTACHMENT);
            int foodCount = foodList.getFoodEatenCount();

            updateAllManagersForPlayer(player, foodCount);
        }
    }


    public static void syncFoodListAndUpdateAllManagersForPlayer(Player player) {
        syncFoodListAndUpdateAllManagersForPlayer(player, player.getData(FOOD_LIST_ATTACHMENT).getFoodEatenCount());
    }

    public static void syncFoodListAndUpdateAllManagersForPlayer(Player player, int foodCount) {
        player.syncData(SolExtended.FOOD_LIST_ATTACHMENT);
        updateAllManagersForPlayer(player, player.getData(SolExtended.FOOD_LIST_ATTACHMENT).getFoodEatenCount());
    }

    private static void updateAllManagersForPlayer(Player player) {
        updateAllManagersForPlayer(player, player.getData(FOOD_LIST_ATTACHMENT).getFoodEatenCount());
    }


    private static void updateAllManagersForPlayer(Player player, int foodCount) {

        if (IS_DEV && Constants.ENABLE_EXTENSIVE_LOGGING) {
            SolExtended.LOGGER.warn("MilestoneManagerRegistry.updateAllManagers: Updating all managers for player {} with foodCount {}",
                    player.getName().getString(), foodCount);
        }

        for (ManagerRegistration<?> registration : REGISTERED_MANAGERS.values()) {
            updateManager(player, foodCount, registration);
        }
    }

    private static <M extends MilestoneManager<?>> void updateManager(Player player, int foodCount, ManagerRegistration<M> registration) {
        try {
            M manager = player.getData(registration.attachmentType);
            manager.onFoodCountUpdate(player, foodCount);
        } catch (Exception e) {
            SolExtended.LOGGER.error(
                    "Failed to update milestone manager {} for player {}",
                    registration.id,
                    player.getName().getString(),
                    e
            );
        }
    }

}
