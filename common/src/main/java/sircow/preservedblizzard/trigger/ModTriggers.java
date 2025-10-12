package sircow.preservedblizzard.trigger;

import net.minecraft.advancements.CriterionTrigger;
import sircow.preservedblizzard.trigger.custom.*;

import java.util.List;
import java.util.function.Supplier;

public class ModTriggers {
    public static class TriggerEntry<T extends CriterionTrigger<?>> {
        public final String id;
        public final Supplier<T> factory;
        public CriterionTrigger<?> trigger;

        public TriggerEntry(String id, Supplier<T> factory) {
            this.id = id;
            this.factory = factory;
        }
    }

    public static final TriggerEntry<WorldJoinTrigger> WORLD_JOIN = new TriggerEntry<>("world_join", WorldJoinTrigger::new);
    public static final TriggerEntry<MasteryAdequateTrigger> MASTERY_ADEQUATE = new TriggerEntry<>("mastery_adequate", MasteryAdequateTrigger::new);
    public static final TriggerEntry<MasteryAdvancedTrigger> MASTERY_ADVANCED = new TriggerEntry<>("mastery_advanced", MasteryAdvancedTrigger::new);
    public static final TriggerEntry<MasteryBeginnerTrigger> MASTERY_BEGINNER = new TriggerEntry<>("mastery_beginner", MasteryBeginnerTrigger::new);
    public static final TriggerEntry<MasteryChampionTrigger> MASTERY_CHAMPION = new TriggerEntry<>("mastery_champion", MasteryChampionTrigger::new);
    public static final TriggerEntry<MasteryDiscipleTrigger> MASTERY_DISCIPLE = new TriggerEntry<>("mastery_disciple", MasteryDiscipleTrigger::new);
    public static final TriggerEntry<MasteryInfernalTrigger> MASTERY_INFERNAL = new TriggerEntry<>("mastery_infernal", MasteryInfernalTrigger::new);
    public static final TriggerEntry<MasteryMasterTrigger> MASTERY_MASTER = new TriggerEntry<>("mastery_master", MasteryMasterTrigger::new);
    public static final TriggerEntry<MasteryNoviceTrigger> MASTERY_NOVICE = new TriggerEntry<>("mastery_novice", MasteryNoviceTrigger::new);
    public static final TriggerEntry<MasteryStarterTrigger> MASTERY_STARTER = new TriggerEntry<>("mastery_starter", MasteryStarterTrigger::new);

    public static final List<TriggerEntry<?>> ALL_TRIGGERS = List.of(
            WORLD_JOIN,
            MASTERY_ADEQUATE,
            MASTERY_ADVANCED,
            MASTERY_BEGINNER,
            MASTERY_CHAMPION,
            MASTERY_DISCIPLE,
            MASTERY_INFERNAL,
            MASTERY_MASTER,
            MASTERY_NOVICE,
            MASTERY_STARTER
    );
}
