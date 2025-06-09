package sircow.preservedblizzard.mixin;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedblizzard.other.IWorldBorderStatus;

@Mixin(PrimaryLevelData.class)
public class PrimaryLevelDataMixin implements IWorldBorderStatus {
    @Unique private boolean worldBorderSet = false;

    @Override
    public boolean isWorldBorderSetPersistent() {
        return this.worldBorderSet;
    }

    @Override
    public void setWorldBorderSetPersistent(boolean set) {
        this.worldBorderSet = set;
    }

    @Inject(method = "parse", at = @At("RETURN"))
    private static <T> void preserved_blizzard$parse(Dynamic<T> tag, LevelSettings levelSettings, PrimaryLevelData.SpecialWorldProperty specialWorldProperty, WorldOptions worldOptions, Lifecycle worldGenSettingsLifecycle, CallbackInfoReturnable<PrimaryLevelData> cir) {
        PrimaryLevelData instance = cir.getReturnValue();
        IWorldBorderStatus borderStatus = (IWorldBorderStatus) instance;
        boolean loadedFlag = tag.get("pblizzard:world_border_set").asBoolean(false);

        borderStatus.setWorldBorderSetPersistent(loadedFlag);
    }

    @Inject(method = "setTagData", at = @At("TAIL"))
    private void preserved_blizzard$setTagData(RegistryAccess registry, CompoundTag nbt, CompoundTag playerNBT, CallbackInfo ci) {
        nbt.putBoolean("pblizzard:world_border_set", this.worldBorderSet);
    }
}
