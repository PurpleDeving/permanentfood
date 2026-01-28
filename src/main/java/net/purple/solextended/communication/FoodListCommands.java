package net.purple.solextended.communication;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
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

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static net.purple.solextended.SolExtended.IS_DEV;

@EventBusSubscriber
public class FoodListCommands {


    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {

        if (IS_DEV) {
            FoodListCommands.registerDevCommands(event.getDispatcher());
        }

    }


    /******************************************
     Dev Commands for testing and debugging
     ******************************************/

    public static void registerDevCommands(CommandDispatcher<CommandSourceStack> dispatcher) {

        final LiteralArgumentBuilder<CommandSourceStack> foodStatsCommand = Commands.literal("foodlist");
        foodStatsCommand//.requires((CommandSourceStack sourceStack) -> sourceStack.hasPermission(2))
                .then(Commands.literal("showstats").executes(FoodListCommands::showStats));
        dispatcher.register(foodStatsCommand);

        final LiteralArgumentBuilder<CommandSourceStack> foodlistCommand = Commands.literal("foodlist");
        foodlistCommand//.requires((CommandSourceStack sourceStack) -> sourceStack.hasPermission(2))
                .then(Commands.literal("showlist").executes(FoodListCommands::showList));
        dispatcher.register(foodlistCommand);

    }


    private static int showStats(CommandContext<CommandSourceStack> context) {

        CommandSourceStack source = context.getSource();

        if (!source.isPlayer()) {
            source.sendFailure(Component.literal("§cThis command can only be executed by a player!"));
            return 0;
        }

        MutableComponent output = Component.literal("");

        // IMPL All FoodList base Stats

        source.sendSuccess(() -> output, true);

        return 1;
    }

    private static int showList(CommandContext<CommandSourceStack> context) {

        CommandSourceStack source = context.getSource();

        if (!source.isPlayer()) {
            source.sendFailure(Component.literal("§cThis command can only be executed by a player!"));
            return 0;
        }

        ServerPlayer player = source.getPlayer();

        assert player != null;
        var foodList = player.getData(SolExtended.FOOD_LIST_ATTACHMENT);
        var eatenFoods = foodList.getEatenFoods();

        // Deterministic + server-friendly sorting: registry id (minecraft:apple) instead of localized names.
        List<ResourceLocation> sortedIds = eatenFoods.stream()
                .map(BuiltInRegistries.ITEM::getKey)
                .filter(Objects::nonNull)
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
