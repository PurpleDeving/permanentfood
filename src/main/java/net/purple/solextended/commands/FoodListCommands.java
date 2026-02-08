package net.purple.solextended.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.purple.solextended.foodlist.FoodList;
import net.purple.solextended.milestonebased.MilestoneManagerRegistry;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

import static net.purple.solextended.SolExtended.FOOD_LIST_ATTACHMENT;
import static net.purple.solextended.SolExtended.IS_DEV;
import static net.purple.solextended.client.LocalizationHelper.keyString;

@EventBusSubscriber
public class FoodListCommands {

    // TODO - Can commands be case sensitive, underscore or space separated?

    // IMPL Translation Keys for List Messages

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {

        FoodListCommands.registerNormalCommands(event.getDispatcher());

        if (IS_DEV) {
            FoodListCommands.registerDevCommands(event.getDispatcher());
        }

    }

    /******************************************
     Normal Commands for players
     ******************************************/


    public static void registerNormalCommands(CommandDispatcher<CommandSourceStack> dispatcher) {

        final LiteralArgumentBuilder<CommandSourceStack> foodlistclearCommand = Commands.literal("foodlist");
        foodlistclearCommand//.requires((CommandSourceStack sourceStack) -> sourceStack.hasPermission(2))
                .then(Commands.literal("clearlist").executes(FoodListCommands::clearList));
        dispatcher.register(foodlistclearCommand);


        final LiteralArgumentBuilder<CommandSourceStack> foodStatsCommand = Commands.literal("foodlist");
        foodStatsCommand//.requires((CommandSourceStack sourceStack) -> sourceStack.hasPermission(2))
                .then(Commands.literal("showstats").executes(FoodListCommands::showStats));
        dispatcher.register(foodStatsCommand);

    }

    /**
     * Runs {@code action} only if the command source is a real {@link ServerPlayer}.
     *
     * <p>This centralizes the null/"not a player" checks so individual command methods
     * stay branch-light and don't have to repeat boilerplate.</p>
     */
    private static int withPlayer(CommandContext<CommandSourceStack> context, ToIntFunction<ServerPlayer> action) {
        CommandSourceStack source = context.getSource();

        if (!source.isPlayer()) {
            source.sendFailure(Component.translatable(keyString("command", "foodlist.commands.only_player"))
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        // On dedicated servers this should be non-null when isPlayer()==true, but keep
        // the check anyway for safety and to provide a localized error.
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(Component.translatable(keyString("command", "foodlist.commands.player_not_found"))
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        return action.applyAsInt(player);
    }

    private static int clearList(CommandContext<CommandSourceStack> context) {
        return withPlayer(context, player -> {
            var source = context.getSource();

            var foodList = player.getData(FOOD_LIST_ATTACHMENT);
            foodList.clearList();

            MilestoneManagerRegistry.syncFoodListAndUpdateAllManagersForPlayer(player, foodList.getFoodEatenCount());

            source.sendSuccess(() -> Component.translatable(keyString("command", "foodlist.clearlist.success")), true);
            return 1;
        });
    }

    private static int showStats(CommandContext<CommandSourceStack> context) {
        return withPlayer(context, player -> {
            var source = context.getSource();

            var foodList = player.getData(FOOD_LIST_ATTACHMENT);
            int foodCount = foodList.getFoodEatenCount();

            MutableComponent output = Component.empty()
                    .append(Component.translatable(keyString("command", "foodlist.showstats.header"), player.getName())
                            .withStyle(ChatFormatting.GOLD))
                    .append("\n")
                    .append(Component.translatable(keyString("command", "foodlist.showstats.foods_eaten"), foodCount)
                            .withStyle(ChatFormatting.GRAY))
                    .append("\n");

            // Let each milestone manager append its own stats lines.
            MilestoneManagerRegistry.outputStatsAllManagers(player, foodCount, output);

            source.sendSuccess(() -> output, false);
            return 1;
        });
    }


    /******************************************
     Dev Commands for testing and debugging
     ******************************************/

    public static void registerDevCommands(CommandDispatcher<CommandSourceStack> dispatcher) {


        final LiteralArgumentBuilder<CommandSourceStack> foodlistCommand = Commands.literal("foodlist");
        foodlistCommand
                .then(Commands.literal("showlist").executes(FoodListCommands::showList));
        dispatcher.register(foodlistCommand);

        final LiteralArgumentBuilder<CommandSourceStack> foodlistAddTestFoodCommand = Commands.literal("foodlist");
        foodlistAddTestFoodCommand
                .then(Commands.literal("testfood")
                        .then(Commands.argument("count", IntegerArgumentType.integer(1))
                                .executes(FoodListCommands::addTestFood)));
        dispatcher.register(foodlistAddTestFoodCommand);

    }


    private static int addTestFood(CommandContext<CommandSourceStack> context) {
        return withPlayer(context, player -> {
            int count = IntegerArgumentType.getInteger(context, "count");
            return addTestFood(player, context.getSource(), count);
        });
    }

    /**
     * Adds up to {@code count} random missing foods to the player's eaten list.
     *
     * @return number of foods actually added (0 if none could be added)
     */
    private static int addTestFood(ServerPlayer player, CommandSourceStack source, int count) {
        var playerFoodList = player.getData(FOOD_LIST_ATTACHMENT);

        // Build missing foods list (allowed foods minus already eaten)

        int countdown = count;

        for (Item item : FoodList.lazzyGetAllowedFoods()) {
            if (playerFoodList.hasEaten(item)) {
                continue;
            }


            playerFoodList.addFood(item);
            countdown--;

            if (countdown <= 0) {
                source.sendSuccess(() -> Component.translatable(keyString("command", "foodlist.testfood.success"), count)
                        .withStyle(ChatFormatting.GREEN), true);
                MilestoneManagerRegistry.syncFoodListAndUpdateAllManagersForPlayer(player);
                return 1;
            }

        }

        int addedCount = count - countdown;
        if (addedCount >= 1) {

            source.sendSuccess(() -> Component.translatable(keyString("command", "foodlist.testfood.success.partial"), addedCount, count)
                    .withStyle(ChatFormatting.GREEN), true);
            MilestoneManagerRegistry.syncFoodListAndUpdateAllManagersForPlayer(player);
            return 1;
        } else {
            source.sendFailure(Component.translatable(keyString("command", "foodlist.testfood.failure.none_added"))
                    .withStyle(ChatFormatting.RED));
            return 0;
        }
    }


    private static int showList(CommandContext<CommandSourceStack> context) {
        return withPlayer(context, player -> {
            CommandSourceStack source = context.getSource();

            var foodList = player.getData(FOOD_LIST_ATTACHMENT);
            var eatenFoods = foodList.getEatenFoods();

            // Deterministic + server-friendly sorting: registry id (minecraft:apple) instead of localized names.
            List<ResourceLocation> sortedIds = eatenFoods.stream()
                    .map(BuiltInRegistries.ITEM::getKey)
                    .sorted(Comparator.comparing(ResourceLocation::toString, String.CASE_INSENSITIVE_ORDER))
                    .toList();

            MutableComponent output = Component.literal("")
                    .append("§6Eaten Foods (§f" + sortedIds.size() + "§6):\n");

            if (sortedIds.isEmpty()) {
                output.append("§7(none)\n");
            } else {
                for (ResourceLocation id : sortedIds) {
                    Item item = BuiltInRegistries.ITEM.get(id);
                    // Show both registry id and display name to make dev debugging easier.
                    output.append("§7- §f" + id + " §8(" + item.getDescriptionId() + ")\n");
                }
            }

            source.sendSuccess(() -> output, true);
            return 1;
        });
    }
}
