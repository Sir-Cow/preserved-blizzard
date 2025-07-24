package sircow.preservedblizzard.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
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
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof Player) {
            // if the entity is a player, always allow drops
        }
        else if (damageSource.getEntity() instanceof Player || damageSource.getEntity() instanceof IronGolem) {
            // if the entity is a mob and killed by a player or iron golem, allow drops
        }
        else {
            // otherwise, cancel drops
            ci.cancel();
        }
    }
}
