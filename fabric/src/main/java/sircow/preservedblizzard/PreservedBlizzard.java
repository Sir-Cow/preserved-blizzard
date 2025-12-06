package sircow.preservedblizzard;

import net.fabricmc.api.ModInitializer;
import sircow.preservedblizzard.effect.FabricModEffects;
import sircow.preservedblizzard.other.FabricModEvents;

public class PreservedBlizzard implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonClass.init();
        FabricModEffects.registerFabricModEffects();
        FabricModEvents.registerModEvents();
    }
}
