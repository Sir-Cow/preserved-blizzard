package sircow.preservedblizzard.other;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import sircow.preservedblizzard.network.ModMessages;

import java.util.*;

public class WorldDataManager {
    public static final Map<String, Component> RANK_PREFIXES = new HashMap<>();
    public static final Map<String, Component> RANK_SUFFIXES = new HashMap<>();

    static {
        RANK_PREFIXES.put("starter", Component.literal("\uE001 "));
        RANK_PREFIXES.put("beginner", Component.literal("\uE002 "));
        RANK_PREFIXES.put("intermediate", Component.literal("\uE003 "));
        RANK_PREFIXES.put("adequate", Component.literal("\uE004 "));
        RANK_PREFIXES.put("advanced", Component.literal("\uE005 "));
        RANK_PREFIXES.put("master", Component.literal("\uE006 "));
        RANK_PREFIXES.put("infernal", Component.literal("\uE007 "));
        RANK_PREFIXES.put("placeholder", Component.literal("\uE000 "));

        RANK_SUFFIXES.put("starter", Component.empty());
        RANK_SUFFIXES.put("beginner", Component.empty());
        RANK_SUFFIXES.put("intermediate", Component.empty());
        RANK_SUFFIXES.put("adequate", Component.empty());
        RANK_SUFFIXES.put("advanced", Component.empty());
        RANK_SUFFIXES.put("master", Component.empty());
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

    public static Set<ResourceLocation> getPlayerAwardedAdvancements(MinecraftServer server, UUID playerUUID) {
        return getWorldData(server).playerAwardedAdvancements.getOrDefault(playerUUID, new HashSet<>());
    }

    public static void addPlayerAwardedAdvancement(MinecraftServer server, UUID playerUUID, ResourceLocation advancement) {
        Set<ResourceLocation> advancements = getWorldData(server).playerAwardedAdvancements.computeIfAbsent(playerUUID, k -> new HashSet<>());
        advancements.add(advancement);
        getWorldData(server).setDirty();
    }
}
