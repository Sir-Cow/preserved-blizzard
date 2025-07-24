package sircow.preservedblizzard.other;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import sircow.preservedblizzard.Constants;
import sircow.preservedblizzard.effect.ModEffects;
import sircow.preservedblizzard.network.ModMessages;
import sircow.preservedblizzard.platform.Services;
import sircow.preservedblizzard.trigger.ModTriggers;

import java.util.*;

public class FabricModEvents {
    private static MinecraftServer currentServer;

    public static void removeTridentDropFromDrowned() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            // prevent drowned dropping trident
            if (entity instanceof Drowned drowned) {
                if (drowned.getMainHandItem().is(Items.TRIDENT)) {
                    drowned.setDropChance(EquipmentSlot.MAINHAND, 0.0f);
                }
            }
        });
    }

    public static void handleEntityDeath() {
        ServerLivingEntityEvents.ALLOW_DEATH.register((livingEntity, damageSource, damageAmount) -> {
            if (livingEntity instanceof Player player) {
                TempInventoryStorage.savePlayerInventory(player);
            }
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

    public static void removeEffectWhenPlayerDamagesHostile() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!(entity instanceof Monster)) {
                return true;
            }

            if (source.getEntity() instanceof ServerPlayer player) {
                if (player.hasEffect(ModEffects.SUNSHINE_GRACE)) {
                    player.removeEffect(ModEffects.SUNSHINE_GRACE);
                }
            }
            return true;
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
                                serverPlayer.displayClientMessage(Component.translatable("effect.pblizzard.well_rested_awake_not_sleeping", player.getName()), true);
                            }
                        }
                    }
                }
            }
        });
    }

    // masteries
    private static String getPlayerRankId(UUID playerUuid) {
        if (currentServer == null) {
            Constants.LOG.warn("currentServer is null when trying to get player rank for {}", playerUuid);
            return "";
        }
        return WorldDataManager.getPlayerRank(currentServer, playerUuid);
    }

    private static Component getPrefixForRank(String rankId) {
        return WorldDataManager.RANK_PREFIXES.getOrDefault(rankId, Component.empty());
    }

    private static Component getSuffixForRank(String rankId) {
        return WorldDataManager.RANK_SUFFIXES.getOrDefault(rankId, Component.empty());
    }

    private static void createOrUpdateAllRankTeams() {
        if (currentServer == null) return;

        Scoreboard scoreboard = currentServer.getScoreboard();

        String[] rankIds = {"starter", "beginner", "novice", "disciple", "adequate", "advanced", "master", "champion", "infernal", "placeholder"};
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
        String scoreboardEntry = player.getScoreboardName();
        PlayerTeam currentTeam = scoreboard.getPlayersTeam(scoreboardEntry);
        if (currentTeam != null && !currentTeam.getName().equals(playerRankId)) {
            scoreboard.removePlayerFromTeam(scoreboardEntry);
        }

        PlayerTeam targetTeam = scoreboard.getPlayerTeam(playerRankId);
        if (targetTeam == null) {
            targetTeam = scoreboard.addPlayerTeam(playerRankId);
            targetTeam.setPlayerPrefix(getPrefixForRank(playerRankId));
            targetTeam.setPlayerSuffix(getSuffixForRank(playerRankId));
        }

        if (!targetTeam.getPlayers().contains(scoreboardEntry)) {
            scoreboard.addPlayerToTeam(scoreboardEntry, targetTeam);
        }
    }


    public static void initialiseMasteries() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            currentServer = server;
            createOrUpdateAllRankTeams();
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                WorldDataManager.syncPlayerPointsWithAdvancements(server, player);
            }
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> currentServer = null);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.player;
            if (!Services.PLATFORM.isModLoaded("pinferno")) {
                ModTriggers.WORLD_JOIN.trigger(player);
            }
            assignPlayerToRankTeam(player);
            WorldDataManager.syncPlayerPointsWithAdvancements(server, player);

            int currentPoints = WorldDataManager.getPlayerPoints(server, player.getUUID());
            ServerPlayNetworking.send(player, new ModMessages.PlayerPointsPayload(player.getUUID(), currentPoints));
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
        // Constants.LOG.info("Registering Fabric Mod Events for " + Constants.MOD_ID);
        removeTridentDropFromDrowned();
        handleEntityDeath();
        removeEffectWhenPlayerDamagesHostile();
        modifySleeping();
        initialiseMasteries();
        if (Services.PLATFORM.isModLoaded("pinferno")) {
            checkInitialAdvancement();
        }
    }
}
