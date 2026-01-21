package net.purple.permfood.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.purple.permfood.moddata.attributes.ModAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

import java.util.List;

import static net.purple.permfood.config.Config.ENABLE_HUNGER_ON_PEACEFUL;
import static net.purple.permfood.config.Config.PEACEFUL_HUNGER_DIFFICULTY;
import static net.purple.permfood.config.ConfigAttributes.EXHAUSTION_PER_HEAL;
import static net.purple.permfood.moddata.attributes.ModAttributes.MAX_EXHAUSTION;
import static net.purple.permfood.moddata.attributes.ModAttributes.MAX_SATURATION;

@Mixin(FoodData.class)
public class FoodDataMixin {

    /******************************************
     Caching
     ******************************************/


    @Unique
    private Player permanentfood_1_21_1$player;


    /******************************************
     Max Hunger Scaling
     ******************************************/

    // Should also fix saturation because its limit is the current foodLevel
    @ModifyConstant(
            method = "add",
            constant = @Constant(intValue = 20,
                    ordinal = 0) // The one in the first Mth.clamp
    )
    private int fixAddWithMaxHunger(int original) {
        permanentfood_1_21_1$validatePlayer();
        return (int) this.permanentfood_1_21_1$player.getAttribute(ModAttributes.MAX_HUNGER).getValue(); //TODO Rounding ?
    }

    @Unique
    @OnlyIn(Dist.CLIENT)
    private void permanentfood_1_21_1$updateClientPlayer() {
        if (this.permanentfood_1_21_1$player == null) {
            this.permanentfood_1_21_1$player = Minecraft.getInstance().player;
        }
    }

    //Limited to Server Side but not with @OnlyIn because local server is also Client side.
    @Unique
    private void permanentfood_1_21_1$updateServerPlayer() {

        if (this.permanentfood_1_21_1$player == null) {


            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server == null) {
                return;
            }
            List<ServerPlayer> players = server.getPlayerList().getPlayers();

            for (ServerPlayer player : players) {
                if (player.getFoodData() == (FoodData) (Object) this) {
                    this.permanentfood_1_21_1$player = player;
                    return;
                }
            }
        }
    }

    @Unique
    private void permanentfood_1_21_1$validatePlayer() {
        if (EffectiveSide.get().isServer()) {
            permanentfood_1_21_1$updateServerPlayer();
        }


        if (EffectiveSide.get().isClient()) {
            permanentfood_1_21_1$updateClientPlayer();
        }
    }


    @ModifyConstant(
            method = "needsFood()Z",
            constant = @Constant(intValue = 20,
                    ordinal = 0) // @Only Value in the Method
    )
    private int needsFoodMaxHungerCheck(int original) {

        permanentfood_1_21_1$validatePlayer();

        return (int) this.permanentfood_1_21_1$player.getAttribute(ModAttributes.MAX_HUNGER).getValue(); //TODO Rounding ?
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
        //UpdateServerPlayer runs through the first mixin first.
        permanentfood_1_21_1$validatePlayer();
        float myCustomMax = (float) this.permanentfood_1_21_1$player.getAttribute(MAX_SATURATION).getValue();
        return Mth.clamp(saturationToClamp, min, myCustomMax);
    }

    /******************************************
     PEACEFUL HUNGER
     ******************************************/

    /// What difficulty should be used when you has HUNGER_ON_PEACEFUL on ?
    @ModifyVariable(method = "tick",
            at = @At("STORE"),
            name = "difficulty")
    private Difficulty peaceful_hunger$tick$getDifficulty(Difficulty originalHungerDifficulty) {
        if (ENABLE_HUNGER_ON_PEACEFUL.get() && originalHungerDifficulty == Difficulty.PEACEFUL) {
            return PEACEFUL_HUNGER_DIFFICULTY.get();
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
    private int setThresholdForNaturalRegenerationWithSaturation(int original, Player player) {
        //TODO return player.getData(PLAYER_VALUES).getNatural_regen_threshold_with_saturation();
        return original;
    }

    // Hunger Threshold for Natural_Regeneration without Saturation
    @ModifyConstant(
            method = "tick", // Note: The method descriptor is (IF)V in bytecode
            constant = @Constant(intValue = 18,
                    ordinal = 0) // The under the Natural_Regen Block
    )
    private int setThresholdForNaturalRegenerationNoSaturation(int original, Player player) {
        // Todo      return player.getData(PLAYER_VALUES).getNatural_regen_threshold_no_saturation();
        return original;
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
        return (float) player.getAttribute(MAX_EXHAUSTION).getValue();
    }

    // Exhaustion per Heal
    // MAX Exhaustion
    @ModifyConstant(
            method = "tick", // Note: The method descriptor is (IF)V in bytecode
            constant = @Constant(floatValue = 6.0F)
    )
    private float useExhaustionForHealing(float original, Player player) {
        return EXHAUSTION_PER_HEAL.get().floatValue();
    }


}