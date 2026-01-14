package net.purple.permfood.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;


import java.util.Collection;

import static net.purple.permfood.config.Config.*;
import static net.purple.permfood.moddata.ModData.PLAYER_VALUES;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Shadow
    @Final
    private Collection<MutableComponent> prefixes;

    @Shadow
    public abstract float getDigSpeed(BlockState p_36282_, @Nullable BlockPos pos);

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


        if (ENABLE_HUNGER_ON_PEACEFUL.get()) {
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

        return ((Player) (Object) this).getData(PLAYER_VALUES).getMax_saturation();
    }

    //TODO Add Buff Implementation on Player.eat()

}

