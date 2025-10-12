package sircow.preservedblizzard;

import net.fabricmc.api.ModInitializer;
import sircow.preservedblizzard.effect.FabricModEffects;
import sircow.preservedblizzard.network.ModMessages;
import sircow.preservedblizzard.other.FabricModEvents;
import sircow.preservedblizzard.trigger.FabricModTriggers;

public class PreservedBlizzard implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonClass.init();
        FabricModEffects.registerFabricModEffects();
        FabricModTriggers.registerFabricTriggers();
        FabricModEvents.registerModEvents();
        ModMessages.registerMessages();
    }
}
