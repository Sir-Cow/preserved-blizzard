package sircow.preservedblizzard.other;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import sircow.preservedblizzard.Constants;
import sircow.preservedblizzard.network.ModMessages;

import java.util.*;

public class WorldDataManager {
    public static final Map<String, Component> RANK_PREFIXES = new HashMap<>();
    public static final Map<String, Component> RANK_SUFFIXES = new HashMap<>();
    private static final List<String> RANK_ORDER = List.of(
            "",
            "starter",
            "beginner",
            "novice",
            "disciple",
            "adequate",
            "advanced",
            "master",
            "champion",
            "infernal"
    );

    static {
        RANK_PREFIXES.put("starter", Component.literal("\uE001 "));
        RANK_PREFIXES.put("beginner", Component.literal("\uE002 "));
        RANK_PREFIXES.put("novice", Component.literal("\uE003 "));
        RANK_PREFIXES.put("disciple", Component.literal("\uE004 "));
        RANK_PREFIXES.put("adequate", Component.literal("\uE005 "));
        RANK_PREFIXES.put("advanced", Component.literal("\uE006 "));
        RANK_PREFIXES.put("master", Component.literal("\uE007 "));
        RANK_PREFIXES.put("champion", Component.literal("\uE008 "));
        RANK_PREFIXES.put("infernal", Component.literal("\uE009 "));
        RANK_PREFIXES.put("placeholder", Component.literal("\uE000 "));

        RANK_SUFFIXES.put("starter", Component.empty());
        RANK_SUFFIXES.put("beginner", Component.empty());
        RANK_SUFFIXES.put("novice", Component.empty());
        RANK_SUFFIXES.put("disciple", Component.empty());
        RANK_SUFFIXES.put("adequate", Component.empty());
        RANK_SUFFIXES.put("advanced", Component.empty());
        RANK_SUFFIXES.put("master", Component.empty());
        RANK_SUFFIXES.put("champion", Component.empty());
        RANK_SUFFIXES.put("infernal", Component.empty());
        RANK_SUFFIXES.put("placeholder", Component.empty());
    }

    private WorldDataManager() {
    }

    public static ModWorldData getWorldData(MinecraftServer server) {
        return ModWorldData.get(server);
    }

    public static int getPlayerPoints(MinecraftServer server, UUID playerUUID) {
        return getWorldData(server).playerPoints.getOrDefault(playerUUID, 0);
    }

    public static void setPlayerPoints(MinecraftServer server, UUID playerUUID, int points) {
        getWorldData(server).playerPoints.put(playerUUID, points);
        getWorldData(server).setDirty();

        ServerPlayer player = server.getPlayerList().getPlayer(playerUUID);
        if (player != null) {
            ServerPlayNetworking.send(player, new ModMessages.PlayerPointsPayload(playerUUID, points));
            for (ServerPlayer other : server.getPlayerList().getPlayers()) {
                if (!other.getUUID().equals(playerUUID)) {
                    ServerPlayNetworking.send(other, new ModMessages.PlayerPointsPayload(playerUUID, points));
                }
            }
        }
    }

    public static String getPlayerRank(MinecraftServer server, UUID playerUUID) {
        return getWorldData(server).PLAYER_RANKS.getOrDefault(playerUUID, "");
    }

    public static void setPlayerRank(MinecraftServer server, UUID playerUUID, String rank) {
        getWorldData(server).PLAYER_RANKS.put(playerUUID, rank);
        getWorldData(server).setDirty();
    }

    public static void syncPlayerPointsWithAdvancements(MinecraftServer server, ServerPlayer player) {
        UUID playerUUID = player.getUUID();
        ModWorldData data = getWorldData(server);
        int recalculatedPoints = 0;
        Set<ResourceLocation> newAwardedAdvancements = new HashSet<>();
        for (AdvancementHolder holder : server.getAdvancements().getAllAdvancements()) {
            ResourceLocation id = holder.id();
            if (!ModAdvancements.EXCLUDED_ADVANCEMENTS.contains(id) && !id.getPath().startsWith("recipes/")) {
                AdvancementProgress progress = player.getAdvancements().getOrStartProgress(holder);
                if (progress.isDone()) {
                    Advancement advancement = holder.value();
                    DisplayInfo display = advancement.display().orElse(null);
                    if (display != null) {
                        switch (display.getType()) {
                            case TASK -> recalculatedPoints += 1;
                            case GOAL -> recalculatedPoints += 3;
                            case CHALLENGE -> recalculatedPoints += 7;
                        }
                    }
                    newAwardedAdvancements.add(id);
                }
            }
        }

        data.playerAwardedAdvancements.put(playerUUID, newAwardedAdvancements);
        setPlayerPoints(server, playerUUID, recalculatedPoints);
        String oldRank = getPlayerRank(server, playerUUID);
        String newRank = calculateRank(server, player, recalculatedPoints);
        if (!oldRank.equals(newRank)) {
            setPlayerRank(server, playerUUID, newRank);
            FabricModEvents.assignPlayerToRankTeam(player);
            if (isRankUp(oldRank, newRank)) {
                String prefixString = RANK_PREFIXES.getOrDefault(newRank, Component.empty()).getString();
                if (prefixString.endsWith(" ")) {
                    prefixString = prefixString.substring(0, prefixString.length() - 1);
                }
                MutableComponent message = Component.translatable(
                        "message.pblizzard.mastery_rank_up",
                        player.getName(),
                        Component.literal(prefixString)
                ).withStyle(style -> style
                        .withHoverEvent(new HoverEvent.ShowText(Component.translatable("message.pblizzard.mastery_rank_up_hover")))
                        .withClickEvent(new ClickEvent.SuggestCommand("/msg " + player.getName().getString() + " GG!"))
                );

                for (ServerPlayer onlinePlayer : server.getPlayerList().getPlayers()) {
                    onlinePlayer.sendSystemMessage(message);
                }
            }
            removeMasteryAdvancementIfDowngraded(player, oldRank, newRank);
        }
    }

    public static String calculateRank(MinecraftServer server, ServerPlayer player, int points) {
        boolean hasAll = server.getAdvancements().getAllAdvancements().stream()
                .filter(holder -> !ModAdvancements.EXCLUDED_ADVANCEMENTS.contains(holder.id()))
                .filter(holder -> !holder.id().getPath().startsWith("recipes/"))
                .allMatch(holder -> player.getAdvancements().getOrStartProgress(holder).isDone());

        if (hasAll) return "infernal";
        if (points >= 300) return "champion";
        if (points >= 240) return "master";
        if (points >= 160) return "advanced";
        if (points >= 100) return "adequate";
        if (points >= 50)  return "disciple";
        if (points >= 30)  return "novice";
        if (points >= 15)  return "beginner";
        if (points >= 6)   return "starter";
        return "";
    }

    private static void removeMasteryAdvancementIfDowngraded(ServerPlayer player, String oldRank, String newRank) {
        if (oldRank.isEmpty() || oldRank.equals(newRank)) return;

        int playerPoints = getPlayerPoints(player.getServer(), player.getUUID());

        boolean stillQualifies = switch (oldRank) {
            case "starter" -> playerPoints >= 6;
            case "beginner" -> playerPoints >= 15;
            case "novice" -> playerPoints >= 30;
            case "disciple" -> playerPoints >= 50;
            case "adequate" -> playerPoints >= 100;
            case "advanced" -> playerPoints >= 160;
            case "master" -> playerPoints >= 240;
            case "champion" -> playerPoints >= 300;
            case "infernal" -> calculateRank(Objects.requireNonNull(player.getServer()), player, playerPoints).equals("infernal");
            default -> false;
        };

        if (stillQualifies) return;

        ResourceLocation oldRankAdvancement = switch (oldRank) {
            case "starter" -> Constants.id("mastery/starter");
            case "beginner" -> Constants.id("mastery/beginner");
            case "novice" -> Constants.id("mastery/novice");
            case "disciple" -> Constants.id("mastery/disciple");
            case "adequate" -> Constants.id("mastery/adequate");
            case "advanced" -> Constants.id("mastery/advanced");
            case "master" -> Constants.id("mastery/master");
            case "champion" -> Constants.id("mastery/champion");
            case "infernal" -> Constants.id("mastery/infernal");
            default -> null;
        };

        if (oldRankAdvancement != null) {
            AdvancementHolder holder = Objects.requireNonNull(player.getServer()).getAdvancements().get(oldRankAdvancement);
            if (holder != null) {
                AdvancementProgress progress = player.getAdvancements().getOrStartProgress(holder);
                for (String criterion : progress.getCompletedCriteria()) {
                    player.getAdvancements().revoke(holder, criterion);
                }
            }
        }
    }

    public static boolean isRankUp(String oldRank, String newRank) {
        return RANK_ORDER.indexOf(newRank) > RANK_ORDER.indexOf(oldRank);
    }
}
