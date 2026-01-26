package net.purple.permfood.mixin;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.purple.permfood.config.Configs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.purple.permfood.moddata.attributes.ModAttributes.MAX_SATURATION;

@Mixin(Player.class)
public abstract class PlayerMixin {

    /******************************************
     PEACEFUL HUNGER
     ******************************************/

    @Redirect(
            method = "aiStep()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getDifficulty()Lnet/minecraft/world/Difficulty;",
                    ordinal = 0 // The Peaceful check
            )
    )
    private Difficulty changeNaturalRegenAccordingToPeacefulHunger(Level level) {


        if (Configs.foodSystemConfig.peacefulHungerSection.ENABLE_HUNGER_ON_PEACEFUL) {
            // Correct difficulty doesn't matter. If you want Hunger_on_Peaceful, then we can just return anything that is not peaceful
            return Difficulty.EASY;
        }

        return level.getDifficulty();
    }

    /******************************************
     Max Saturation Scaling
     ******************************************/

    // Not needed for hunger because that uses FoodData.needsFood()
    @ModifyConstant(
            method = "aiStep()V",
            constant = @Constant(floatValue = 20.0F,
                    ordinal = 0) // @after the peaceful check from above
    )
    private float FixNaturalRegionWithCorrectMax(float original) {

        return (float) ((Player) (Object) this).getAttribute(MAX_SATURATION).getValue();
    }

    //TODO Add Buff Implementation on Player.eat()

}

