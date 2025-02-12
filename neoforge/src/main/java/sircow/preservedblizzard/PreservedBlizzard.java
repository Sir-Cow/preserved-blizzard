package sircow.preservedblizzard;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class PreservedBlizzard {
    public PreservedBlizzard(IEventBus eventBus) {
        CommonClass.init();
    }
}
