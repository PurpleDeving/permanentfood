package net.purple.permfood.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.purple.permfood.attributes.ModAttributes;
import net.purple.permfood.config.Configs;
import net.purple.permfood.config.FoodSystemConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

import static net.purple.permfood.attributes.ModAttributes.*;


@SuppressWarnings("DataFlowIssue")
@Mixin(FoodData.class)
public class FoodDataMixin {

    /******************************************
     Caching Player
     ******************************************/


    // Attached to the Player Instance
    @Unique
    private Player permanentfood_1_21_1$player;


    @Unique
    private void permanentfood_1_21_1$validatePlayer() {
        if (EffectiveSide.get().isServer()) {
            permanentfood_1_21_1$updateServerPlayer();
        }


        if (EffectiveSide.get().isClient()) {
            permanentfood_1_21_1$updateClientPlayer();
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
    @OnlyIn(Dist.CLIENT)
    private void permanentfood_1_21_1$updateClientPlayer() {
        if (this.permanentfood_1_21_1$player == null) {
            this.permanentfood_1_21_1$player = Minecraft.getInstance().player;
        }
    }


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
        if (!Configs.foodSystemConfig.sectionHunger.ENABLE_HUNGER_CHANGES) {
            return original;
        }

        permanentfood_1_21_1$validatePlayer();
        return (int) Math.round(Objects.requireNonNull(this.permanentfood_1_21_1$player.getAttribute(ModAttributes.MAX_HUNGER)).getValue());
    }


    @ModifyConstant(
            method = "needsFood()Z",
            constant = @Constant(intValue = 20,
                    ordinal = 0) // @Only Value in the Method
    )
    private int needsFoodMaxHungerCheck(int original) {
        if (!Configs.foodSystemConfig.sectionHunger.ENABLE_HUNGER_CHANGES) {
            return original;
        }

        permanentfood_1_21_1$validatePlayer();
        return (int) Math.round(Objects.requireNonNull(this.permanentfood_1_21_1$player.getAttribute(ModAttributes.MAX_HUNGER)).getValue());
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
        float returnMax = original;
        if (Configs.foodSystemConfig.sectionSaturation.ENABLE_SATURATION_CHANGES) {
            //UpdateServerPlayer runs through the first mixin first.
            permanentfood_1_21_1$validatePlayer();
            returnMax = (float) this.permanentfood_1_21_1$player.getAttribute(MAX_SATURATION).getValue();
        }

        return Mth.clamp(saturationToClamp, min, returnMax);
    }


    @Shadow
    private int lastFoodLevel;
    @Shadow
    private int foodLevel;
    @Shadow
    private float exhaustionLevel;
    @Shadow
    private float saturationLevel;
    @Shadow
    private int tickTimer;


    /**
     * Overwrites the vanilla addExhaustion method to use a dynamic maximum exhaustion cap
     * based on the player's MAX_EXHAUSTION attribute multiplied by 10, instead of the
     * hardcoded 40.0F value.
     *
     * @param exhaustion The amount of exhaustion to add
     * @reason Replace hardcoded 40.0F cap with configurable max exhaustion * 10
     * @author PurpleMod
     */
    @Overwrite
    public void addExhaustion(float exhaustion) {
        permanentfood_1_21_1$validatePlayer();
        float maxExhaustionCap = Configs.foodSystemConfig.sectionExhaustion.ENABLE_EXHAUSTION_CHANGES
                ? (float) this.permanentfood_1_21_1$player.getAttribute(MAX_EXHAUSTION).getValue() * 10
                : 40.0F;
        this.exhaustionLevel = Math.min(this.exhaustionLevel + exhaustion, maxExhaustionCap);
    }


    /******************************************
     Tick Overwrite
     ******************************************/

    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tickOverwrite(Player player, CallbackInfo ci) {
        FoodSystemConfig foodSystemConfig = Configs.foodSystemConfig;
        Difficulty difficulty = player.level().getDifficulty();

        // Only when it's originally easy, do you have to
        if (foodSystemConfig.sectionFoodHealing.ENABLE_HUNGER_DIFFICULTY && difficulty == Difficulty.PEACEFUL) {
            difficulty = Configs.foodSystemConfig.sectionFoodHealing.HUNGER_DIFFICULTY.get();
        }

        this.lastFoodLevel = foodLevel;

        boolean isExhaustionChanged = foodSystemConfig.sectionExhaustion.ENABLE_EXHAUSTION_CHANGES;
        float exhaustionThreshold = isExhaustionChanged ? (float) player.getAttribute(MAX_EXHAUSTION).getValue() : 4.0F;
        boolean isHungerChanged = foodSystemConfig.sectionHunger.ENABLE_HUNGER_CHANGES;
        int maxHunger = isHungerChanged ? (int) (player.getAttribute(MAX_HUNGER).getValue() + 0.5) : 20;
        float exhaustionPerHeal = foodSystemConfig.sectionFoodHealing.exhaustionPerHeal.get();

        // Reduce Exhaustion for the cost of food/saturation
        if (this.exhaustionLevel > exhaustionThreshold) {
            this.exhaustionLevel -= exhaustionThreshold;
            if (this.saturationLevel > 0.0F) {
                this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
            } else if (difficulty != Difficulty.PEACEFUL) {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }

        boolean flag = player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
        // Superfast healing. Slowed if you don't have as much saturation as a Heal would add exhaustion. Adds Exhaustion for the amount healed
        if (flag && this.saturationLevel > 0.0F && player.isHurt() && this.foodLevel >= maxHunger) {
            ++this.tickTimer;
            if (this.tickTimer >= 10) {
                float f = Math.min(this.saturationLevel, exhaustionPerHeal);
                player.heal(f / exhaustionPerHeal);
                this.addExhaustion(f);
                this.tickTimer = 0;
            }
            ci.cancel();
            return;

            // Normal Healing for exhaustion. Threshold is as Vanilla 90% of the maxHunger
        } else if (flag && this.foodLevel >= (int) (maxHunger * 0.9 + 0.5) && player.isHurt()) {
            ++this.tickTimer;
            if (this.tickTimer >= 80) {
                player.heal(1.0F);
                this.addExhaustion(exhaustionPerHeal);
                this.tickTimer = 0;
            }
            ci.cancel();
            return;
        }

        // Food Starvation
        if (this.foodLevel <= 0) {
            ++this.tickTimer;
            if (this.tickTimer >= 80) {
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
                    player.hurt(player.damageSources().starve(), 1.0F);
                }

                this.tickTimer = 0;
            }
        } else {
            this.tickTimer = 0; // Tick timer is only used by natural regeneration or when starving. So if booth doesn't happen, reset it.
        }


        ci.cancel(); // Prevents orriginal logic
    }


}