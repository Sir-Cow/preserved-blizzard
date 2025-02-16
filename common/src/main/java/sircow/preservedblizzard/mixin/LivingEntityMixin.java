package sircow.preservedblizzard.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    // make mob loot only drop when killed by a player
    @Inject(method = "dropAllDeathLoot", at = @At("HEAD"), cancellable = true)
    private void preserved_blizzard$modifyMobDrops(ServerLevel level, DamageSource damageSource, CallbackInfo ci) {
        if (damageSource.getEntity() instanceof Player) {
            // nothing! continue as normal
        } else {
            ci.cancel();
        }
    }
}
