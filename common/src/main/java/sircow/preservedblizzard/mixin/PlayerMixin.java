package sircow.preservedblizzard.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedblizzard.effect.ModEffects;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "die", at = @At(value = "HEAD"))
    private void preserved_blizzard$handleDie(DamageSource damageSource, CallbackInfo ci) {
        if (!this.isSpectator() && this.level() instanceof ServerLevel serverLevel && !this.hasEffect(ModEffects.WELL_RESTED)) {
            this.dropAllDeathLoot(serverLevel, damageSource);
        }
    }

    @Inject(method = "dropEquipment", at = @At("HEAD"), cancellable = true)
    public void preserved_blizzard$preventInvDrop(ServerLevel level, CallbackInfo ci) {
        Player player = (Player)(Object)this;
        if (player.hasEffect(ModEffects.WELL_RESTED)) {
            ci.cancel();
        }
    }

    @Inject(method = "getBaseExperienceReward", at = @At("HEAD"), cancellable = true)
    public void preserved_blizzard$preventExpDrop(ServerLevel level, CallbackInfoReturnable<Integer> cir) {
        Player player = (Player)(Object)this;
        if (player.hasEffect(ModEffects.WELL_RESTED) || player.isSpectator()) {
            cir.setReturnValue(0);
            cir.cancel();
        }
    }
}
