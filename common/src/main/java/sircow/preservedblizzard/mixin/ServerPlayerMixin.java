package sircow.preservedblizzard.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedblizzard.other.IPlayerTimeData;

import java.util.Optional;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin implements IPlayerTimeData {
    @Unique private Optional<Integer> onlineTimeTicks = Optional.of(0);

    @Override
    public Optional<Integer> getOnlineTimeTicks() {
        return this.onlineTimeTicks;
    }

    @Override
    public void setOnlineTimeTicks(int ticks) {
        this.onlineTimeTicks = Optional.of(ticks);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putInt("pblizzard:online_time_ticks", this.onlineTimeTicks.orElseThrow());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void yourModId_readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        if (compoundTag.contains("pblizzard:online_time_ticks")) {
            this.onlineTimeTicks = compoundTag.getInt("pblizzard:online_time_ticks");
        }
        else {
            this.onlineTimeTicks = Optional.of(0);
        }
    }
}
