package sircow.preservedblizzard.effect;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.util.List;
import java.util.function.Supplier;

public class ModEffects {
    public static class EffectEntry {
        public final String id;
        public final Supplier<MobEffect> factory;
        public Holder<MobEffect> holder;

        public EffectEntry(String id, Supplier<MobEffect> factory) {
            this.id = id;
            this.factory = factory;
        }
    }

    public static final EffectEntry WELL_RESTED = new EffectEntry("well_rested", () -> new WellRestedEffect(MobEffectCategory.BENEFICIAL, 0xE3884E));
    public static final EffectEntry SUNSHINE_GRACE = new EffectEntry("sunshine_grace", () -> new SunshineGraceEffect(MobEffectCategory.BENEFICIAL, 0xE3884E));

    public static final List<EffectEntry> ALL_EFFECTS = List.of(
            WELL_RESTED,
            SUNSHINE_GRACE
    );
}
