package sircow.preservedblizzard.trigger;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import sircow.preservedblizzard.Constants;

import java.util.HashMap;
import java.util.Map;

public class NeoForgeModTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(
            BuiltInRegistries.TRIGGER_TYPES,
            Constants.MOD_ID
    );

    private static final Map<ModTriggers.TriggerEntry<?>, Holder<? extends CriterionTrigger<?>>> registryMap = new HashMap<>();

    public static void init(IEventBus modEventBus) {
        registerNeoForgeTriggers();
        TRIGGERS.register(modEventBus);
        modEventBus.addListener(NeoForgeModTriggers::commonSetup);
    }

    public static void registerNeoForgeTriggers() {
        for (ModTriggers.TriggerEntry<?> entry : ModTriggers.ALL_TRIGGERS) {
            Holder<? extends CriterionTrigger<?>> reg = TRIGGERS.register(entry.id, entry.factory);
            registryMap.put(entry, reg);
        }
    }

    private static void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(NeoForgeModTriggers::assignToCommon);
    }

    public static void assignToCommon() {
        for (Map.Entry<ModTriggers.TriggerEntry<?>, Holder<? extends CriterionTrigger<?>>> entry : registryMap.entrySet()) {
            entry.getKey().trigger = entry.getValue().value();
        }
    }
}
