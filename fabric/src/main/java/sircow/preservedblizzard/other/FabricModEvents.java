package sircow.preservedblizzard.other;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import sircow.preservedblizzard.CommonClass;
import sircow.preservedblizzard.Constants;
import sircow.preservedblizzard.effect.ModEffects;
import sircow.preservedblizzard.network.ModMessages;
import sircow.preservedblizzard.platform.Services;
import sircow.preservedblizzard.trigger.ModTriggers;

import java.util.*;

public class FabricModEvents {
    private static MinecraftServer currentServer;

    public static void removeTridentDropFromDrowned() {
        ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, damageSource) -> {
            if (livingEntity instanceof Drowned drowned) {
                ItemStack heldItem = drowned.getItemInHand(InteractionHand.MAIN_HAND);
                if (heldItem.getItem() instanceof TridentItem) {
                    drowned.setDropChance(EquipmentSlot.MAINHAND, 0);
                }
            }
        });
    }

    public static void handleEntityDeath() {
        ServerPlayerEvents.ALLOW_DEATH.register((player, damageSource, damageAmount) -> {
            TempInventoryStorage.savePlayerInventory(player);
            return true;
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            boolean hadWellRestedEffectOnDeath = TempInventoryStorage.restorePlayerInventory(newPlayer);

            // display message if player had well rested effect
            if (hadWellRestedEffectOnDeath) {
                Objects.requireNonNull(newPlayer.getServer()).execute(() -> newPlayer.sendSystemMessage(Component.translatable("effect.pblizzard.well_rested_consume"), true));
            }
        });
    }

    public static void modifySleeping() {
        EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
            if (entity instanceof Player player) {
                if (player.getSleepTimer() > 20 && !player.level().isMoonVisible()) {
                    MinecraftServer server = player.level().getServer();
                    if (server != null) {
                        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
                            serverPlayer.addEffect(new MobEffectInstance(ModEffects.WELL_RESTED, 24000, 0, false, false, true));
                            if (player.getUUID() == serverPlayer.getUUID()) {
                                serverPlayer.displayClientMessage(Component.translatable("effect.pblizzard.well_rested_awake"), true);
                            }
                            else {
                                serverPlayer.displayClientMessage(Component.translatable("effect.pblizzard.well_rested_awake_not_sleeping"), true);
                            }
                        }
                    }
                }
            }
        });
    }


    // masteries
    private static void addRank(String rankId, Component prefix, Component suffix) {
        CommonClass.RANK_PREFIXES.put(rankId, prefix);
        CommonClass.RANK_SUFFIXES.put(rankId, suffix);
    }

    private static String getPlayerRankId(UUID playerUuid) {
        return CommonClass.PLAYER_RANKS.getOrDefault(playerUuid, "");
    }

    private static Component getPrefixForRank(String rankId) {
        return CommonClass.RANK_PREFIXES.getOrDefault(rankId, Component.empty());
    }

    private static Component getSuffixForRank(String rankId) {
        return CommonClass.RANK_SUFFIXES.getOrDefault(rankId, Component.empty());
    }

    private static void createOrUpdateAllRankTeams() {
        if (currentServer == null) return;

        Scoreboard scoreboard = currentServer.getScoreboard();

        String[] rankIds = {"starter", "beginner", "intermediate", "adequate", "advanced", "master", "infernal", "placeholder"};
        for (String rankId : rankIds) {
            PlayerTeam playerTeam = scoreboard.getPlayerTeam(rankId);

            if (playerTeam == null) {
                playerTeam = scoreboard.addPlayerTeam(rankId);
            }

            playerTeam.setPlayerPrefix(getPrefixForRank(rankId));
            playerTeam.setPlayerSuffix(getSuffixForRank(rankId));
        }
    }

    public static void assignPlayerToRankTeam(ServerPlayer player) {
        if (currentServer == null) return;

        Scoreboard scoreboard = currentServer.getScoreboard();
        String playerRankId = getPlayerRankId(player.getUUID());

        PlayerTeam currentTeam = scoreboard.getPlayersTeam(player.getName().getString());
        if (currentTeam != null && !currentTeam.getName().equals(playerRankId)) {
            scoreboard.removePlayerFromTeam(player.getName().getString());
        }

        PlayerTeam targetTeam = scoreboard.getPlayerTeam(playerRankId);
        if (targetTeam == null) {
            targetTeam = scoreboard.addPlayerTeam(playerRankId);
            targetTeam.setPlayerPrefix(getPrefixForRank(playerRankId));
            targetTeam.setPlayerSuffix(getSuffixForRank(playerRankId));
        }

        scoreboard.addPlayerToTeam(player.getName().getString(), targetTeam);
    }

    public static void initialiseMasteries() {
        addRank("starter", Component.literal("\uE001 "), Component.empty());
        addRank("beginner", Component.literal("\uE002 "), Component.empty());
        addRank("intermediate", Component.literal("\uE003 "), Component.empty());
        addRank("adequate", Component.literal("\uE004 "), Component.empty());
        addRank("advanced", Component.literal("\uE005 "), Component.empty());
        addRank("master", Component.literal("\uE006 "), Component.empty());
        addRank("infernal", Component.literal("\uE007 "), Component.empty());
        addRank("placeholder", Component.literal("\uE000 "), Component.empty());

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            currentServer = server;
            createOrUpdateAllRankTeams();
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            currentServer = null;
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (!CommonClass.PLAYER_RANKS.containsKey(handler.player.getUUID())) {
                if (!Services.PLATFORM.isModLoaded("pinferno")) {
                    ModTriggers.WORLD_JOIN.trigger(handler.player);
                }
            }
            assignPlayerToRankTeam(handler.player);

            UUID playerUUID = handler.player.getUUID();
            int points = CommonClass.playerPoints.getOrDefault(playerUUID, 0);
            ServerPlayNetworking.send(handler.player, new ModMessages.PlayerPointsPayload(playerUUID, points));
        });
    }

    public static void checkInitialAdvancement() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                var advancement = server.getAdvancements().get(ResourceLocation.withDefaultNamespace("story/root"));
                if (advancement == null) continue;
                if (player.getAdvancements().getOrStartProgress(advancement).isDone()) {
                    ModTriggers.WORLD_JOIN.trigger(player);
                }
            }
        });
    }

    public static void registerModEvents() {
        Constants.LOG.info("Registering Fabric Mod Events for " + Constants.MOD_ID);
        removeTridentDropFromDrowned();
        handleEntityDeath();
        modifySleeping();
        initialiseMasteries();
        if (Services.PLATFORM.isModLoaded("pinferno")) {
            checkInitialAdvancement();
        }
    }
}
