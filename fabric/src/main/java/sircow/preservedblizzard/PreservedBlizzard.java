package sircow.preservedblizzard;

import net.fabricmc.api.ModInitializer;
import sircow.preservedblizzard.network.ModMessages;
import sircow.preservedblizzard.other.FabricModEvents;

public class PreservedBlizzard implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonClass.init();
        FabricModEvents.registerModEvents();
        ModMessages.registerMessages();
    }
}
