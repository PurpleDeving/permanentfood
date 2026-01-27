/*
package net.purple.permfood.mixin;


import com.cazsius.solcarrot.command.FoodListCommand;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;
import net.purple.permfood.moddata.attributes.PlayerAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoodListCommand.class)
public class FoodListCommandMixin {

    @Inject(method = "clearFoodList", at = @At("TAIL"))
    private static void clearFoodList(CommandContext<CommandSourceStack> context, Player target, CallbackInfoReturnable<Integer> cir) {
        PlayerAttributes playerAttributes = PlayerAttributes.getOrCreatePlayerAttributes(target);
        playerAttributes.updateFoodCount(); // If the clearFoodList is successful, the foodCount will be set to 0
        playerAttributes.updateBuffs();
    }
}
*/
