package sircow.preservedblizzard.mixin;

import net.minecraft.advancements.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sircow.preservedblizzard.other.FabricModEvents;
import sircow.preservedblizzard.other.ModAdvancements;
import sircow.preservedblizzard.other.WorldDataManager;
import sircow.preservedblizzard.trigger.ModTriggers;

@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {
    @Shadow private ServerPlayer player;

    @Inject(method = "award", at = @At("RETURN"))
    private void preserved_blizzard$onAwardAdvancement(AdvancementHolder advancementHolder, String criterionKey, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        MinecraftServer server = player.getServer();
        if (server == null) return;

        ResourceLocation advancementId = advancementHolder.id();
        if (ModAdvancements.EXCLUDED_ADVANCEMENTS.contains(advancementId) || advancementId.getPath().startsWith("recipes/")) {
            return;
        }

        WorldDataManager.syncPlayerPointsWithAdvancements(server, player);

        int currentPoints = WorldDataManager.getPlayerPoints(server, player.getUUID());
        String oldRank = WorldDataManager.getPlayerRank(server, player.getUUID());
        String newRank = WorldDataManager.calculateRank(server, player, currentPoints);

        if (!oldRank.equals(newRank)) {
            WorldDataManager.setPlayerRank(server, player.getUUID(), newRank);
            FabricModEvents.assignPlayerToRankTeam(player);
            triggerRankAdvancement(newRank, player);
        }
        else {
            triggerRankAdvancement(newRank, player);
        }
    }

    @Inject(method = "revoke", at = @At("RETURN"))
    private void preserved_blizzard$onRevokeAdvancement(AdvancementHolder advancementHolder, String criterionKey, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        MinecraftServer server = player.getServer();
        if (server == null) return;

        WorldDataManager.syncPlayerPointsWithAdvancements(server, player);
    }

    @Unique
    private void triggerRankAdvancement(String rank, ServerPlayer player) {
        switch (rank) {
            case "infernal" -> ModTriggers.MASTERY_INFERNAL.trigger(player);
            case "champion" -> ModTriggers.MASTERY_CHAMPION.trigger(player);
            case "master" -> ModTriggers.MASTERY_MASTER.trigger(player);
            case "advanced" -> ModTriggers.MASTERY_ADVANCED.trigger(player);
            case "adequate" -> ModTriggers.MASTERY_ADEQUATE.trigger(player);
            case "disciple" -> ModTriggers.MASTERY_DISCIPLE.trigger(player);
            case "novice" -> ModTriggers.MASTERY_NOVICE.trigger(player);
            case "beginner" -> ModTriggers.MASTERY_BEGINNER.trigger(player);
            case "starter" -> ModTriggers.MASTERY_STARTER.trigger(player);
        }
    }
}
