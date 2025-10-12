package sircow.preservedblizzard;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import sircow.preservedblizzard.effect.NeoForgeModEffects;
import sircow.preservedblizzard.trigger.NeoForgeModTriggers;

@Mod(Constants.MOD_ID)
public class PreservedBlizzard {
    public PreservedBlizzard(IEventBus eventBus) {
        CommonClass.init();
        NeoForgeModEffects.init(eventBus);
        NeoForgeModTriggers.init(eventBus);
    }
}
