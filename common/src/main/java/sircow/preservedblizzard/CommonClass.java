package sircow.preservedblizzard;

import sircow.preservedblizzard.block.ModBlocks;
import sircow.preservedblizzard.item.ModItemGroups;
import sircow.preservedblizzard.item.ModItems;
import sircow.preservedblizzard.platform.Services;

public class CommonClass {
    public static void init() {
        if (Services.PLATFORM.isModLoaded("preservedblizzard")) {
            Constants.LOG.info("Initialising Preserved: Blizzard");
            ModBlocks.registerModBlocks();
            ModItems.registerModItems();
            ModItemGroups.registerItemGroups();
        }
    }
}