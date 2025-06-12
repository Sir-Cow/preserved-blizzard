package sircow.preservedblizzard.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import sircow.preservedblizzard.Constants;

public class ModEffects {
    public static final Holder<MobEffect> WELL_RESTED = register("well_rested",
            new WellRestedEffect(MobEffectCategory.BENEFICIAL, 0xE3884E));
    public static final Holder<MobEffect> SUNSHINE_GRACE = register("sunshine_grace",
            new SunshineGraceEffect(MobEffectCategory.BENEFICIAL, 0xE3884E));

    private static Holder<MobEffect> register(String name, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Constants.id(name), effect);
    }

    public static void registerModEffects() {
        Constants.LOG.info("Registering Mod Effects for " + Constants.MOD_ID);
    }
}
