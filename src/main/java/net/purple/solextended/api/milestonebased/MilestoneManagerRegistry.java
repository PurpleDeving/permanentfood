package net.purple.solextended.api.milestonebased;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.purple.solextended.SolExtended;

import java.util.*;
import java.util.function.Supplier;

/**
 * Central registry for all milestone managers across all mods.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Mods register manager types via {@link #registerManager(String, String, Supplier)}.</li>
 *   <li>solextended stores per-player instances using DataAttachments.</li>
 *   <li>Call {@link #updateAllManagers(Player, int)} to refresh all registered managers for one player.</li>
 *   <li>Call {@link #updateAllPlayersAllManagers(Collection)} to refresh all players (e.g. config reload).</li>
 * </ul>
 */
public class MilestoneManagerRegistry {

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

    private static class ManagerRegistration<M extends MilestoneManager<?>> {
        final ResourceLocation id;
        final Supplier<AttachmentType<M>> attachmentType;

        ManagerRegistration(ResourceLocation id, Supplier<AttachmentType<M>> attachmentType) {
            this.id = id;
            this.attachmentType = attachmentType;
        }
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
    public static <M extends MilestoneManager<?>> M getManagerForPlayer(
            Player player,
            Supplier<AttachmentType<M>> attachmentType
    ) {
        return player.getData(attachmentType);
    }

    /**
     * Updates all registered milestone managers for a specific player.
     */
    public static void updateAllManagers(Player player, int foodCount) {
        for (ManagerRegistration<?> registration : REGISTERED_MANAGERS.values()) {
            updateManager(player, foodCount, registration);
        }
    }

    private static <M extends MilestoneManager<?>> void updateManager(
            Player player,
            int foodCount,
            ManagerRegistration<M> registration
    ) {
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

    /**
     * Updates all milestone managers for all online players.
     * Useful for config reloads or server-wide updates.
     */
    public static void updateAllPlayersAllManagers(Collection<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            net.purple.solextended.foodlist.PlayerFoodList foodList =
                    player.getData(SolExtended.FOOD_LIST_ATTACHMENT);
            int foodCount = foodList.getFoodEatenCount();

            updateAllManagers(player, foodCount);
        }
    }

    public static Set<ResourceLocation> getRegisteredManagerIds() {
        return Collections.unmodifiableSet(REGISTERED_MANAGERS.keySet());
    }

    public static int getRegisteredManagerCount() {
        return REGISTERED_MANAGERS.size();
    }
}
