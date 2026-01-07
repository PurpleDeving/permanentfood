package net.purple.permfood;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jline.utils.Log;

import java.util.Collection;

import static net.purple.permfood.PermanentFood.MODID;
import static net.purple.permfood.PlayerValueHandler.PLAYER_VALUES;

public class ModCommands {


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        Log.warn("Is this reachable 3321");
        final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(MODID);
        root//.requires((CommandSourceStack sourceStack) -> sourceStack.hasPermission(2))
                .then(Commands.literal("showstats").executes(ModCommands::showStats))

                .then(Commands.literal("testy").executes(ModCommands::useTest));
        dispatcher.register(root);

    }

    private static int useTest(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(
                () -> Component.literal("§aCommand works!"),
                false
        );
        return 1;

    }


    private static int showStats(CommandContext<CommandSourceStack> context) {
        
        System.out.println("Can you sout here?");
        CommandSourceStack source = context.getSource();


        // Check if source is a player
        if (!source.isPlayer()) {
            PermanentFood.LOG.warn("§cThis command can only be executed by a player! 543");
            source.sendFailure(Component.literal("§cThis command can only be executed by a player!"));
            return 0;
        }

        ServerPlayer player = source.getPlayer();
        assert player != null;
        PlayerValues values = PLAYER_VALUES.get(player.getUUID());

        //Log.warn(player.getName());
        if (values == null) {
            source.sendFailure(Component.literal("§cNo stats found for " + player.getName().getString()));
            return 0;
        }

        MutableComponent output = Component.literal("§6=== Stats for " + player.getName().getString() + " ===\n")
                .append("§7Unique Foods Eaten: §f" + values.getFoodCount() + "\n")
                .append("§7Max Hunger: §f" + values.getMax_hunger() + "\n")
                .append("§7Max Saturation: §f" + values.getMax_saturation() + "\n")
                .append("§7Max Exhaustion: §f" + values.getMax_exhaustion() + "\n");

        //Log.warn(output);


        source.sendSuccess(() -> output, true);
        return 1;
    }
}
