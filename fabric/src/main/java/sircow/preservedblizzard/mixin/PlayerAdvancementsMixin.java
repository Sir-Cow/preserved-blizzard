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
import sircow.preservedblizzard.trigger.custom.*;

@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {
    @Shadow private ServerPlayer player;

    @Inject(method = "award", at = @At("RETURN"))
    private void preserved_blizzard$onAwardAdvancement(AdvancementHolder advancementHolder, String criterionKey, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        MinecraftServer server = player.level().getServer();

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
        WorldDataManager.syncPlayerPointsWithAdvancements(player.level().getServer(), player);
    }

    @Unique
    private void triggerRankAdvancement(String rank, ServerPlayer player) {
        switch (rank) {
            case "infernal" -> ((MasteryInfernalTrigger) ModTriggers.MASTERY_INFERNAL.trigger).trigger(player);
            case "champion" -> ((MasteryChampionTrigger) ModTriggers.MASTERY_CHAMPION.trigger).trigger(player);
            case "master" -> ((MasteryMasterTrigger) ModTriggers.MASTERY_MASTER.trigger).trigger(player);
            case "advanced" -> ((MasteryAdvancedTrigger) ModTriggers.MASTERY_ADVANCED.trigger).trigger(player);
            case "adequate" -> ((MasteryAdequateTrigger) ModTriggers.MASTERY_ADEQUATE.trigger).trigger(player);
            case "disciple" -> ((MasteryDiscipleTrigger) ModTriggers.MASTERY_DISCIPLE.trigger).trigger(player);
            case "novice" -> ((MasteryNoviceTrigger) ModTriggers.MASTERY_NOVICE.trigger).trigger(player);
            case "beginner" -> ((MasteryBeginnerTrigger) ModTriggers.MASTERY_BEGINNER.trigger).trigger(player);
            case "starter" -> ((MasteryStarterTrigger) ModTriggers.MASTERY_STARTER.trigger).trigger(player);
        }
    }
}
