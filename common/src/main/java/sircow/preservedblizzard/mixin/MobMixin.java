package sircow.preservedblizzard.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedblizzard.effect.ModEffects;

@Mixin(Mob.class)
public class MobMixin {
    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void preserved_blizzard$preventTargetWithSunshineGrace(LivingEntity target, CallbackInfo ci) {
        if (target instanceof Player player) {
            if (player.hasEffect(ModEffects.SUNSHINE_GRACE)) {
                if (player.getY() >= 64.0D) {
                    ci.cancel();
                }
            }
        }
    }
}
