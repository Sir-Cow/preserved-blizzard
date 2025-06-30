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

public class MasteryMasterTrigger implements CriterionTrigger<MasteryMasterTrigger.Instance> {
    private final Map<PlayerAdvancements, Set<Listener<MasteryMasterTrigger.Instance>>> listeners = new HashMap<>();

    @Override
    public @NotNull Codec<MasteryMasterTrigger.Instance> codec() {
        return MasteryMasterTrigger.Instance.CODEC;
    }

    @Override
    public void addPlayerListener(@NotNull PlayerAdvancements playerAdvancements, @NotNull Listener<MasteryMasterTrigger.Instance> listener) {
        this.listeners.computeIfAbsent(playerAdvancements, k -> new HashSet<>()).add(listener);
    }

    @Override
    public void removePlayerListener(@NotNull PlayerAdvancements playerAdvancements, @NotNull Listener<MasteryMasterTrigger.Instance> listener) {
        Set<Listener<MasteryMasterTrigger.Instance>> set = this.listeners.get(playerAdvancements);
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
        Set<Listener<MasteryMasterTrigger.Instance>> set = this.listeners.get(player.getAdvancements());
        if (set != null) {
            for (Listener<MasteryMasterTrigger.Instance> listener : set) {
                listener.run(player.getAdvancements());
            }
        }
    }

    public static class Instance implements CriterionTriggerInstance {
        public static final Codec<MasteryMasterTrigger.Instance> CODEC = Codec.unit(new MasteryMasterTrigger.Instance());

        public Instance() {}

        @Override
        public void validate(@NotNull CriterionValidator validator) {}
    }
}
