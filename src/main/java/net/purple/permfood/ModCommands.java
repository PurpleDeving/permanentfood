package net.purple.permfood;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.purple.permfood.moddata.PlayerValues;

import static net.purple.permfood.Constants.resourceLocationFoodArmorBuff;
import static net.purple.permfood.Constants.resourceLocationFoodArmorToughnessBuff;
import static net.purple.permfood.PermanentFood.MODID;
import static net.purple.permfood.moddata.ModData.PLAYER_VALUES;

public class ModCommands {


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(MODID);
        root//.requires((CommandSourceStack sourceStack) -> sourceStack.hasPermission(2))
                .then(Commands.literal("showstats").executes(ModCommands::showStats));
        dispatcher.register(root);

    }


    public static void onCommandRegister(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
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
        PlayerValues values = player.getData(PLAYER_VALUES);

        MutableComponent output = Component.literal("§6=== Stats for " + player.getName().getString() + " ===\n")
                .append("§7Unique Foods Eaten: §f" + values.getFoodCount() + "\n")
                .append("§7Max Hunger: §f" + values.getMax_hunger() + "\n")
                .append("§7Max Saturation: §f" + values.getMax_saturation() + "\n")
                .append("§7Max Exhaustion: §f" + values.getMax_exhaustion() + "\n")
                .append("§7Armor Food Bonus: §f" + player.getAttributes().getInstance(Attributes.ARMOR).getModifier(resourceLocationFoodArmorBuff).amount() + "\n")
                .append("§7Armor Toughness Food Bonus: §f" + player.getAttributes().getInstance(Attributes.ARMOR).getModifier(resourceLocationFoodArmorToughnessBuff).amount() + "\n");

        source.sendSuccess(() -> output, true);
        return 1;
    }
}
