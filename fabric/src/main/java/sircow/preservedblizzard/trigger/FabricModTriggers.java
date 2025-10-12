package sircow.preservedblizzard.trigger;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import sircow.preservedblizzard.Constants;

public class FabricModTriggers {
    public static void registerFabricTriggers() {
        for (ModTriggers.TriggerEntry<?> entry : ModTriggers.ALL_TRIGGERS) {
            entry.trigger = Registry.register(
                    BuiltInRegistries.TRIGGER_TYPES,
                    Constants.id(entry.id),
                    entry.factory.get()
            );
        }
    }
}
