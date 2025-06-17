package sircow.preservedblizzard.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedblizzard.effect.ModEffects;
import sircow.preservedblizzard.other.IFirstJoinTracker;

import java.util.Optional;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements IFirstJoinTracker {
    @Unique private boolean hasCheckedFirstJoin = false;
    @Unique private Optional<Boolean> hasJoinedBefore = Optional.empty();

    public ServerPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @Override
    public Optional<Boolean> preserved_blizzard$getHasJoinedBefore() {
        return hasJoinedBefore;
    }

    @Override
    public void preserved_blizzard$setHasJoinedBefore(Optional<Boolean> value) {
        this.hasJoinedBefore = value;
    }

    @Override
    public boolean preserved_blizzard$getHasCheckedFirstJoin() {
        return hasCheckedFirstJoin;
    }

    @Override
    public void preserved_blizzard$setHasCheckedFirstJoin(boolean value) {
        this.hasCheckedFirstJoin = value;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void preserved_blizzard$addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        CompoundTag data = new CompoundTag();
        data.putBoolean("hasJoinedBefore", true);
        compoundTag.put("pblizzard:firstJoin", data);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void preserved_blizzard$readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        Optional<CompoundTag> maybeData = compoundTag.getCompound("pblizzard:firstJoin");

        if (maybeData.isPresent()) {
            CompoundTag data = maybeData.get();
            this.hasJoinedBefore = data.getBoolean("hasJoinedBefore").or(() -> Optional.of(false));
        }
        else {
            this.hasJoinedBefore = Optional.of(false);
        }
    }

    @WrapOperation(method = "restoreFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean preserved_blizzard$modifyKeepInventoryRule(GameRules instance, GameRules.Key<GameRules.BooleanValue> key, Operation<Boolean> original) {
        ServerPlayer self = (ServerPlayer)(Object)this;
        if (key == GameRules.RULE_KEEPINVENTORY) {
            return original.call(instance, key) || self.hasEffect(ModEffects.WELL_RESTED);
        }

        return original.call(instance, key);
    }

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void preserved_blizzard$copyFirstJoinState(ServerPlayer oldPlayer, boolean keepEverything, CallbackInfo ci) {
        if (oldPlayer instanceof IFirstJoinTracker oldTracker) {
            IFirstJoinTracker newTracker = this;
            newTracker.preserved_blizzard$setHasJoinedBefore(oldTracker.preserved_blizzard$getHasJoinedBefore());
            newTracker.preserved_blizzard$setHasCheckedFirstJoin(oldTracker.preserved_blizzard$getHasCheckedFirstJoin());
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void preserved_blizzard$onTick(CallbackInfo ci) {
        if (!hasCheckedFirstJoin) {
            hasCheckedFirstJoin = true;

            if (hasJoinedBefore.isEmpty()) {
                hasJoinedBefore = Optional.of(false);
            }

            if (!hasJoinedBefore.get()) {
                this.addEffect(new MobEffectInstance(ModEffects.SUNSHINE_GRACE, 20 * 60 * 10, 0));
                hasJoinedBefore = Optional.of(true);
            }
        }
    }
}
