package sircow.preservedblizzard.trigger.custom;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.CriterionValidator;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MasteryNoviceTrigger implements CriterionTrigger<MasteryNoviceTrigger.Instance> {
    private final Map<PlayerAdvancements, Set<Listener<MasteryNoviceTrigger.Instance>>> listeners = new HashMap<>();

    @Override
    public @NotNull Codec<MasteryNoviceTrigger.Instance> codec() {
        return MasteryNoviceTrigger.Instance.CODEC;
    }

    @Override
    public void addPlayerListener(@NotNull PlayerAdvancements playerAdvancements, @NotNull Listener<MasteryNoviceTrigger.Instance> listener) {
        this.listeners.computeIfAbsent(playerAdvancements, k -> new HashSet<>()).add(listener);
    }

    @Override
    public void removePlayerListener(@NotNull PlayerAdvancements playerAdvancements, @NotNull Listener<MasteryNoviceTrigger.Instance> listener) {
        Set<Listener<MasteryNoviceTrigger.Instance>> set = this.listeners.get(playerAdvancements);
        if (set != null) {
            set.remove(listener);
            if (set.isEmpty()) {
                this.listeners.remove(playerAdvancements);
            }
        }
    }

    @Override
    public void removePlayerListeners(@NotNull PlayerAdvancements playerAdvancements) {
        this.listeners.remove(playerAdvancements);
    }

    public void trigger(ServerPlayer player) {
        Set<Listener<MasteryNoviceTrigger.Instance>> set = this.listeners.get(player.getAdvancements());
        if (set != null) {
            for (Listener<MasteryNoviceTrigger.Instance> listener : set) {
                listener.run(player.getAdvancements());
            }
        }
    }

    public static class Instance implements CriterionTriggerInstance {
        public static final Codec<MasteryNoviceTrigger.Instance> CODEC = Codec.unit(new MasteryNoviceTrigger.Instance());

        public Instance() {}

        @Override
        public void validate(@NotNull CriterionValidator validator) {}
    }
}
