package net.purple.permfood.moddata;

import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.purple.permfood.moddata.attributes.PlayerAttributes;

import static net.purple.permfood.Constants.SOL_CARROT;
import static net.purple.permfood.PermanentFood.MODID;
import static net.purple.permfood.moddata.attributes.PlayerAttributes.PLAYER_ATTRIBUTES;

@EventBusSubscriber(modid = MODID)
public final class PlayerValueEvents {


    //Booth Sides
    @SubscribeEvent(priority = EventPriority.HIGHEST) // Guaranteed to trigger before solcarrot
    public static void updateClientBefore(LivingEntityUseItemEvent.Finish event) {
        if (EffectiveSide.get().isClient()) {
            updatePlayerOnClient();
            //Log.warn("On Client the value is: " + event.getEntity().getData(PLAYER_VALUES).getFoodCount());
        }
    }


    //Booth Sides
    @SubscribeEvent(priority = EventPriority.LOW) // Guaranteed to trigger after solcarrot
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        // I dont need to limit here, but it makes no sense to update if nothing happened on solcarrot side
        if (SOLCarrotConfig.limitProgressionToSurvival() && player.isCreative()) return;

        // If its not food, what the fuck am I doing here?
        var usedStack = event.getItem();
        if (usedStack.getFoodProperties(player) == null) return;

        if (EffectiveSide.get().isClient()) {

            milestoneMessage();

        } else {
            updatePlayerAttributesAndValues(player);
        }






        /*else{

            PlayerValues prevPlayerValues = player.getData(PLAYER_VALUES);
            updatePlayerAttributesAndValues(player);
            PlayerValues currentPlayerValues = player.getData(PLAYER_VALUES);

        }*/


        // TODO - Booth Attributes and updateValues should print out new reached Milestones to the player ingame chat + particles and all tha.
        //  But should not happen if Milestones are re-calculated on login/dimension change/config reload, therefore do here

    }

    @OnlyIn(Dist.CLIENT)
    private static void milestoneMessage() {

        Player localPlayer = Minecraft.getInstance().player;
        /*

        PlayerValues prevPlayerValues = new PlayerValues(localPlayer.getData(PLAYER_VALUES).getFoodCount());
        List<PlayerValue> prevPlayerValuesList = prevPlayerValues.getMilestoneBasedList();
        updatePlayerOnClient();
        List<PlayerValue> currentPlayerValues = localPlayer.getData(PLAYER_VALUES).getMilestoneBasedList();


        for (int i = 0; i < currentPlayerValues.size(); i++) {

            int prevMilestonesReached = prevPlayerValuesList.get(i).getMilestonesReached();
            int currentMilestonesReached = currentPlayerValues.get(i).getMilestonesReached();


            //Log.warn("For Milestone: " + prevPlayerValues.get(i).getName() + " the old number is: " + prevMilestonesReached);
            //Log.warn("For Milestone: " + currentPlayerValues.get(i).getName() + " the new number is: " + currentMilestonesReached);

            if (currentMilestonesReached > prevMilestonesReached) {

                if (SOLCarrotConfig.shouldPlayMilestoneSounds()) {
                    localPlayer.level().playSound(
                            localPlayer,
                            localPlayer.blockPosition(),
                            SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS,
                            1.0F, 1.0F);
                }

                if (SOLCarrotConfig.shouldSpawnMilestoneParticles()) {
                    spawnParticles(localPlayer, ParticleTypes.HEART, 12);

                    if (currentPlayerValues.get(i).maxMilestonesReached()) {
                        spawnParticles(localPlayer, ParticleTypes.HAPPY_VILLAGER, 16);
                    }
                }


            }
        }*/


    }

    private static void spawnParticles(Player player, ParticleOptions type, int count) {
        // hacky way to reuse the existing logic for randomizing particle spawn positions
        var connection = Minecraft.getInstance().getConnection();
        assert connection != null;
        connection.handleParticleEvent(new ClientboundLevelParticlesPacket(
                type, false,
                player.getX(), player.getY() + player.getEyeHeight(), player.getZ(),
                0.5F, 0.5F, 0.5F,
                0.0F, count
        ));
    }

    @OnlyIn(Dist.CLIENT)
    private static void updatePlayerOnClient() {
        Player player = Minecraft.getInstance().player;
        updatePlayerAttributesAndValues(player);
    }


    // Server only and only on actuall Respawn
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {

    }


    // Is Server side only
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {


        Player player = event.getEntity();

        updatePlayerAttributesAndValues(player);

    }


    // Is Server side only
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        updatePlayerAttributesAndValues(player);

    }

    // Unkown if it fires on client
    @SubscribeEvent
    public static void onPlayerDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        updatePlayerAttributesAndValues(player);

    }

    // Unkown if it fires on client. Safety check
    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {

        // RangedAttribute attribute = (RangedAttribute) MAX_HUNGER.value();

        if ((event.getConfig().getModId().equals(MODID) || event.getConfig().getModId().equals(SOL_CARROT)) && EffectiveSide.get().isServer()) {

            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                updatePlayerAttributesAndValues(player);

            }


        }

        if (EffectiveSide.get().isClient()) {
            updatePlayerOnClient(); //TODO if the Attributes are synced this isnt needed ?
        }

    }


    private static void updatePlayerAttributesAndValues(Player player) {

        int foodCount = FoodList.get(player).getProgressInfo().foodsEaten;

        if (!PLAYER_ATTRIBUTES.containsKey(player.getUUID())) {
            PLAYER_ATTRIBUTES.put(player.getUUID(), new PlayerAttributes(player, foodCount));
        }


        // Attribute modifiers
        PlayerAttributes.getPlayerAttributes(player).updateBuffs(foodCount);


    }


}
