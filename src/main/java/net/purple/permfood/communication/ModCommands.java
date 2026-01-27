package net.purple.permfood.communication;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.purple.permfood.PermanentFood;
import net.purple.permfood.moddata.attributes.ModAttributes;
import net.purple.permfood.moddata.attributes.PlayerAttributes;

import static net.purple.permfood.Constants.*;
import static net.purple.permfood.PermanentFood.MODID;

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

        CommandSourceStack source = context.getSource();


        // Check if source is a player
        if (!source.isPlayer()) {
            PermanentFood.LOG.warn("§cThis command can only be executed by a player!");
            source.sendFailure(Component.literal("§cThis command can only be executed by a player!"));
            return 0;
        }

        ServerPlayer player = source.getPlayer();
        AttributeMap attributeMap = player.getAttributes();

        assert player != null;


        AttributeInstance maxHunger = attributeMap.getInstance(ModAttributes.MAX_HUNGER);
        AttributeInstance maxSaturation = attributeMap.getInstance(ModAttributes.MAX_SATURATION);
        AttributeInstance maxExhaustion = attributeMap.getInstance(ModAttributes.MAX_EXHAUSTION);
        AttributeModifier modHunger = maxHunger.getModifier(rLMaxHungerBuff);
        AttributeModifier modSaturation = maxSaturation.getModifier(rLMaxSaturationBuff);
        AttributeModifier modExhaustion = maxExhaustion.getModifier(rLMaxExhaustionBuff);
        // TODO Rounding needed ?
        int maxHungerAmount = (int) maxHunger.getValue();
        double maxSaturationAmount = maxSaturation.getValue();
        double maxExhaustionAmount = maxExhaustion.getValue();


        AttributeModifier modArmor = attributeMap.getInstance(Attributes.ARMOR).getModifier(rLArmorBuff);
        AttributeModifier modArmorTough = attributeMap.getInstance(Attributes.ARMOR_TOUGHNESS).getModifier(rLToughnessBuff);
        AttributeModifier modAttack = attributeMap.getInstance(Attributes.ATTACK_DAMAGE).getModifier(rLAttackDamageBuff);
        AttributeModifier modLuck = attributeMap.getInstance(Attributes.LUCK).getModifier(rLLuckBuff);
        AttributeModifier modKB = attributeMap.getInstance(Attributes.KNOCKBACK_RESISTANCE).getModifier(rLKnockbackResistanceBuff);

        double armorAmount = modArmor != null ? modArmor.amount() : 0.0;
        double armorToughAmount = modArmorTough != null ? modArmorTough.amount() : 0.0;
        double attackAmount = modAttack != null ? modAttack.amount() : 0.0;
        double luckAmount = modLuck != null ? modLuck.amount() : 0.0;
        double kbAmount = modKB != null ? modKB.amount() : 0.0;

        MutableComponent output = Component.literal("§6=== Stats for " + player.getName().getString() + " ===\n")
                .append("§7Unique Foods Eaten: §f" + PlayerAttributes.getOrCreatePlayerAttributes(player).getFoodCount() + "\n")
                .append("§7Max Hunger: §f" + maxHungerAmount + " including a Food Buff of: " + modHunger.amount() + "\n")
                .append("§7Max Saturation: §f" + maxSaturationAmount + " including a Food Buff of: " + modSaturation.amount() + "\n")
                .append("§7Max Exhaustion: §f" + maxExhaustionAmount + " including a Food Buff of: " + modExhaustion.amount() + "\n")
                .append("§7Armor Food Bonus: §f" + armorAmount + "\n")
                .append("§7Armor Toughness Food Bonus: §f" + armorToughAmount + "\n")
                .append("§7Attack Damage Food Bonus: §f" + attackAmount + "\n")
                .append("§7Luck Food Bonus: §f" + luckAmount + "\n")
                .append("§7Knockback Resistance Food Bonus: §f" + kbAmount + "\n");

        source.sendSuccess(() -> output, true);
        return 1;
    }
}
