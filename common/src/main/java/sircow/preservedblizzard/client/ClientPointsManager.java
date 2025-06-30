package sircow.preservedblizzard.client;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientPointsManager {
    private static final Map<UUID, Integer> clientPlayerPoints = new HashMap<>();

    public static void setPlayerPoints(UUID playerUUID, int points) {
        clientPlayerPoints.put(playerUUID, points);
    }

    public static int getPlayerPoints(UUID playerUUID) {
        return clientPlayerPoints.getOrDefault(playerUUID, 0);
    }
}
