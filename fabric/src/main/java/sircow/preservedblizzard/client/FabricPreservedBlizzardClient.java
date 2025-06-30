package sircow.preservedblizzard.client;

import net.fabricmc.api.ClientModInitializer;
import sircow.preservedblizzard.network.ModMessages;

public class FabricPreservedBlizzardClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModMessages.registerS2CPackets();
    }
}
