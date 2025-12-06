package sircow.preservedblizzard.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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

    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
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

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void preserved_blizzard$addAdditionalSaveData(ValueOutput out, CallbackInfo ci) {
        out.putBoolean("pblizzard:hasJoinedBefore", hasJoinedBefore.orElse(false));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void preserved_blizzard$readAdditionalSaveData(ValueInput in, CallbackInfo ci) {
        boolean joinedBefore = in.getBooleanOr("pblizzard:hasJoinedBefore", false);
        this.hasJoinedBefore = Optional.of(joinedBefore);
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
                this.addEffect(new MobEffectInstance(ModEffects.SUNSHINE_GRACE.holder, 20 * 60 * 10, 0));
                hasJoinedBefore = Optional.of(true);
            }
        }
    }
}
