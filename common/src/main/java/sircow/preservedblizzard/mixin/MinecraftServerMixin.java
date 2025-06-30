package sircow.preservedblizzard.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedblizzard.other.IWorldBorderStatus;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Unique private static final int TICKS_PER_HOUR = 20 * 60 * 60;

    @Inject(method = "createLevels", at = @At("TAIL"))
    private void preserved_blizzard$setWorldBorder(ChunkProgressListener listener, CallbackInfo ci) {
        // initial world border setup on world creation
        MinecraftServer server = (MinecraftServer) (Object) this;
        IWorldBorderStatus borderStatus = (IWorldBorderStatus) server.overworld().getLevelData();

        if (!borderStatus.isWorldBorderSetPersistent()) {
            server.overworld().getWorldBorder().setSize(400.00);
            borderStatus.setWorldBorderSetPersistent(true);
        }
    }

    @Inject(method = "tickServer", at = @At("TAIL"))
    private void preserved_blizzard$onServerTick(CallbackInfo ci) {
        // increase the world border size by 1 block
        MinecraftServer server = (MinecraftServer) (Object) this;
        if (server.overworld() == null) {
            return;
        }

        WorldBorder overworldBorder = server.overworld().getWorldBorder();
        double currentDiameter = overworldBorder.getSize();
        int onlinePlayers = server.getPlayerList().getPlayerCount();
        double newSize = getNewSize(currentDiameter, onlinePlayers);
        long lerpTime = 50L;

        if (currentDiameter >= 59999968.0D || onlinePlayers == 0) {
            return;
        }

        if (newSize > currentDiameter) {
            overworldBorder.lerpSizeBetween(currentDiameter, newSize, lerpTime);
        }
        else {
            overworldBorder.setSize(newSize);
        }
    }

    @Unique
    private static double getNewSize(double currentDiameter, int onlinePlayers) {
        double ratePerHour = 60.0D / (Math.log(Math.pow(1.0D + 0.375D * currentDiameter, 2.25D)));
        double finalRatePerHour = ratePerHour * Math.pow((1 + ((double) onlinePlayers / 4)), 2);
        double increasePerTick = finalRatePerHour / TICKS_PER_HOUR;
        double newSize = currentDiameter + increasePerTick;

        if (newSize > 59999968.0D) {
            newSize = 59999968.0D;
        }
        return newSize;
    }
}
