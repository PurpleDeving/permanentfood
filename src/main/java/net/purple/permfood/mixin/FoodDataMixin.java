package net.purple.permfood.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.purple.permfood.Config;
import net.purple.permfood.ConfigCache;
import net.purple.permfood.Constants;
import net.purple.permfood.PermanentFood;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

import java.util.UUID;

import org.jline.utils.Log;


import static net.purple.permfood.ConfigCache.*;
import static net.purple.permfood.PermanentFood.MODID;
import static net.purple.permfood.PlayerValueHandler.PLAYER_VALUES;

import net.neoforged.api.distmarker.Dist.*;

@Mixin(FoodData.class)
public class FoodDataMixin {

    /******************************************
     Caching
     ******************************************/

    @Unique
    private UUID permanentfood_1_21_1$playerUUID;


    @Unique
    private UUID permanentfood_1_21_1$findUUID() {


        if (!(ServerLifecycleHooks.getCurrentServer() == null)) {
            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                if (player.getFoodData() == (FoodData) (Object) this) {
                    return player.getUUID();
                }
            }
        }

        // For the client return the UUID of the ServerPlayer of that LocalPlayer. Needed because it runs on the RenderThread as well

        if (RenderSystem.isOnRenderThread()) {
            Log.warn("Is on Render Thread");
        }

        if (EffectiveSide.get().isClient()) {
            Log.warn("Is on client Side");
        }

        if (Minecraft.getInstance().player == null) {
            Log.warn("No Player was found. This should never happen. Report to " + Constants.MOD_AUTHOR_LONG);
            return null;
        }

        return Minecraft.getInstance().player.getUUID();
    }


    /******************************************
     Max Hunger Scaling
     ******************************************/

    // Should also fix saturation because its limit is the current foodLevel
    @ModifyConstant(
            method = "add", // Note: The method descriptor is (IF)V in bytecode
            constant = @Constant(intValue = 20,
                    ordinal = 0) // The one in the first Mth.clamp
    )
    private int fixAddWithHungerMax(int original) {

        if (permanentfood_1_21_1$playerUUID == null) {
            permanentfood_1_21_1$playerUUID = permanentfood_1_21_1$findUUID();
        }

        // TODO remove if working
        // LocalPlayer detection. If this is needed than findUUID failed
        /*        if (playerUUID == null) {
            return original;
        }*/

        return PLAYER_VALUES.get(this.permanentfood_1_21_1$playerUUID).getMax_hunger();
    }


    @ModifyConstant(
            method = "needsFood()Z",
            constant = @Constant(intValue = 20,
                    ordinal = 0) // @Only Value in the Method
    )
    private int needsFoodMaxHungerCheck(int original) {
        // Return your new maximum saturation value
        return PLAYER_VALUES.get(this.permanentfood_1_21_1$playerUUID).getMax_hunger();
    }

    /******************************************
     Max Saturation Scaling
     ******************************************/

    @Redirect(
            method = "add",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;clamp(FFF)F", // Limiting factor for which Mth.clamp() is used is this Float definition
                    ordinal = 0
            )
    )
    private float redirectSaturationClamp(float saturationToClamp, float min, float original) {
        float myCustomMax = PLAYER_VALUES.get(permanentfood_1_21_1$playerUUID).getMax_saturation();
        return Mth.clamp(saturationToClamp, min, myCustomMax);
    }

    /******************************************
     PEACEFUL HUNGER
     ******************************************/

    /// What difficulty should be used when you has HUNGER_ON_PEACEFUL on ?
    @ModifyVariable(method = "tick",
            at = @At("STORE"),
            ordinal = 0)
    private Difficulty peaceful_hunger$tick$getDifficulty(Difficulty originalHungerDifficulty) {
        if (HUNGER_ON_PEACEFUL_CACHED && originalHungerDifficulty == Difficulty.PEACEFUL) {
            return PEACEFUL_HUNGER_DIFFICULTY_CACHED;
        }
        return originalHungerDifficulty;
    }

    /******************************************
     Natural Regeneration + NON_Natural Regeneration
     ******************************************/

    // Hunger Threshold for Natural_Regeneration with Saturation
    @ModifyConstant(
            method = "tick", // Note: The method descriptor is (IF)V in bytecode
            constant = @Constant(intValue = 20,
                    ordinal = 0) // The one in the Natural_Regen Block
    )
    private int setThresholdForNaturalRegeneration(int original, Player player) {
        return PLAYER_VALUES.get(player.getUUID()).getNatural_regen_threshold_with_saturation();
    }

    // Hunger Threshold for Natural_Regeneration without Saturation
    @ModifyConstant(
            method = "tick", // Note: The method descriptor is (IF)V in bytecode
            constant = @Constant(intValue = 18,
                    ordinal = 0) // The under the Natural_Regen Block
    )
    private int setThresholdForNoNNaturalRegeneration(int original, Player player) {
        return PLAYER_VALUES.get(player.getUUID()).getNatural_regen_threshold_no_saturation();
    }

    /******************************************
     ExhaustionLevel injecting
     ******************************************/

    // MAX Exhaustion
    @ModifyConstant(
            method = "tick", // Note: The method descriptor is (IF)V in bytecode
            constant = @Constant(floatValue = 4.0F)
    )
    private float useMaxExhaustion(float original, Player player) {
        return PLAYER_VALUES.get(player.getUUID()).getMax_exhaustion();
    }

    // Exhaustion per Heal
    // MAX Exhaustion
    @ModifyConstant(
            method = "tick", // Note: The method descriptor is (IF)V in bytecode
            constant = @Constant(floatValue = 6.0F)
    )
    private float useExhaustionForHealing(float original, Player player) {
        return EXHAUSTION_PER_HEAL;
    }


}