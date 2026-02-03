package net.purple.solextended.commands;

import com.mojang.brigadier.CommandDispatcher;
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
import net.purple.solextended.SolExtended;
import net.purple.solextended.api.milestonebased.MilestoneManagerRegistry;

import java.util.Comparator;
import java.util.List;

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

    private static int clearList(CommandContext<CommandSourceStack> context) {

        CommandSourceStack source = context.getSource();

        if (!source.isPlayer()) {
            source.sendFailure(Component.translatable(keyString("command", "foodlist.commands.only_player")));
            return 0;
        }

        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(Component.translatable(keyString("command", "foodlist.commands.player_not_found")));
            return 0;
        }

        player.getData(SolExtended.FOOD_LIST_ATTACHMENT).clearList();
        player.syncData(FOOD_LIST_ATTACHMENT);


        // Direct update for all registered milestone managers
        var foodList = player.getData(SolExtended.FOOD_LIST_ATTACHMENT);
        MilestoneManagerRegistry.updateAllManagers(player, foodList.getFoodEatenCount());

        source.sendSuccess(() -> Component.translatable(keyString("command", "foodlist.clearlist.success")), true);
        return 1;

    }

    private static int showStats(CommandContext<CommandSourceStack> context) {

        CommandSourceStack source = context.getSource();

        if (!source.isPlayer()) {
            source.sendFailure(Component.translatable(keyString("command", "foodlist.commands.only_player")).withStyle(ChatFormatting.RED));
            return 0;
        }

        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(Component.translatable(keyString("command", "foodlist.commands.player_not_found")).withStyle(ChatFormatting.RED));
            return 0;
        }

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
    }


    /******************************************
     Dev Commands for testing and debugging
     ******************************************/

    public static void registerDevCommands(CommandDispatcher<CommandSourceStack> dispatcher) {


        final LiteralArgumentBuilder<CommandSourceStack> foodlistCommand = Commands.literal("foodlist");
        foodlistCommand//.requires((CommandSourceStack sourceStack) -> sourceStack.hasPermission(2))
                .then(Commands.literal("showlist").executes(FoodListCommands::showList));
        dispatcher.register(foodlistCommand);

    }


    private static int showList(CommandContext<CommandSourceStack> context) {

        CommandSourceStack source = context.getSource();

        if (!source.isPlayer()) {
            source.sendFailure(Component.literal("§cThis command can only be executed by a player!"));
            return 0;
        }

        ServerPlayer player = source.getPlayer();

        assert player != null;
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
    }
}
