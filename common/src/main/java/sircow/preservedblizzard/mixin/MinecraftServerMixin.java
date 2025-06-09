package sircow.preservedblizzard.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedblizzard.other.IPlayerTimeData;
import sircow.preservedblizzard.other.IWorldBorderStatus;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Unique private static final int RATE_OF_CHANGE = 20 * 60 * 60;

    @Inject(method = "createLevels", at = @At("TAIL"))
    private void preserved_blizzard$setWorldBorder(ChunkProgressListener listener, CallbackInfo ci) {
        // initial world border setup on world creation
        MinecraftServer server = (MinecraftServer) (Object) this;
        IWorldBorderStatus borderStatus = (IWorldBorderStatus) server.overworld().getLevelData();

        if (!borderStatus.isWorldBorderSetPersistent()) {
            server.overworld().getWorldBorder().setSize(250.0D);
            borderStatus.setWorldBorderSetPersistent(true);
        }
    }

    @Inject(method = "tickServer", at = @At("TAIL"))
    private void preserved_blizzard$onServerTick(CallbackInfo ci) {
        // Increase the world border size by 1 block
        MinecraftServer server = (MinecraftServer) (Object) this;
        if (server.overworld() == null) {
            return;
        }

        WorldBorder overworldBorder = server.overworld().getWorldBorder();
        if (overworldBorder.getSize() < 59999968) {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                IPlayerTimeData playerTimeData = (IPlayerTimeData) player;
                playerTimeData.setOnlineTimeTicks(playerTimeData.getOnlineTimeTicks().get() + 1);

                if (playerTimeData.getOnlineTimeTicks().get() >= RATE_OF_CHANGE) {
                    double currentSize = overworldBorder.getSize();
                    overworldBorder.setSize(currentSize + 2.0D);
                    playerTimeData.setOnlineTimeTicks(playerTimeData.getOnlineTimeTicks().get() - RATE_OF_CHANGE);
                }
            }
        }
    }
}
