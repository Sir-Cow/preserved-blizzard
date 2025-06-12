package sircow.preservedblizzard;

import sircow.preservedblizzard.effect.ModEffects;
import sircow.preservedblizzard.platform.Services;

public class CommonClass {
    public static void init() {
        if (Services.PLATFORM.isModLoaded("pblizzard")) {
            Constants.LOG.info("Initialising Preserved: Blizzard");
            ModEffects.registerModEffects();
        }
    }
}
