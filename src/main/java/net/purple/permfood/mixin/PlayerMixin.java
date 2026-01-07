package net.purple.permfood.mixin;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.purple.permfood.Config;
import net.purple.permfood.PlayerValues;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

import static net.purple.permfood.PlayerValueHandler.PLAYER_VALUES;
import static net.purple.permfood.ConfigCache.*;

@Mixin(Player.class)
public class PlayerMixin {

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
    private Difficulty FixNaturalRegenToConfig(Level level) {


        if (HUNGER_ON_PEACEFUL_CACHED) {
            // Correct difficulty doesnt matter. If you want Hunger_on_Peaceful, then we can just return anything that is not peaceful
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
        // Return your new maximum saturation value

        return PLAYER_VALUES.get(((Player) (Object) this).getUUID()).getMax_saturation(); //TODO Add Buff Impplementation
    }

    //TODO Add Buff Implementation on Player.eat()

}

