package sircow.preservedblizzard.trigger;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import sircow.preservedblizzard.Constants;
import sircow.preservedblizzard.trigger.custom.*;

public class ModTriggers {
    public static final WorldJoinTrigger WORLD_JOIN = register("world_join", new WorldJoinTrigger());
    public static final MasteryAdequateTrigger MASTERY_ADEQUATE = register("mastery_adequate", new MasteryAdequateTrigger());
    public static final MasteryAdvancedTrigger MASTERY_ADVANCED = register("mastery_advanced", new MasteryAdvancedTrigger());
    public static final MasteryBeginnerTrigger MASTERY_BEGINNER = register("mastery_beginner", new MasteryBeginnerTrigger());
    public static final MasteryInfernalTrigger MASTERY_INFERNAL = register("mastery_infernal", new MasteryInfernalTrigger());
    public static final MasteryIntermediateTrigger MASTERY_INTERMEDIATE = register("mastery_intermediate", new MasteryIntermediateTrigger());
    public static final MasteryMasterTrigger MASTERY_MASTER = register("mastery_master", new MasteryMasterTrigger());
    public static final MasteryStarterTrigger MASTERY_STARTER = register("mastery_starter", new MasteryStarterTrigger());

    public static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, Constants.id(name), trigger);
    }

    public static void registerTriggers() {
       // Constants.LOG.info("Registering Mod Triggers for " + Constants.MOD_ID);
    }
}
