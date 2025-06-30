package sircow.preservedblizzard.mixin;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
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
import sircow.preservedblizzard.Constants;
import sircow.preservedblizzard.network.ModMessages;
import sircow.preservedblizzard.other.FabricModEvents;
import sircow.preservedblizzard.other.WorldDataManager;
import sircow.preservedblizzard.trigger.ModTriggers;

import java.util.*;

@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {
    @Shadow private ServerPlayer player;

    @Unique
    private static final List<ResourceLocation> EXCLUDED_ADVANCEMENTS = Arrays.asList(
            Constants.id("mastery/root"),
            Constants.id("mastery/adequate"),
            Constants.id("mastery/advanced"),
            Constants.id("mastery/beginner"),
            Constants.id("mastery/infernal"),
            Constants.id("mastery/intermediate"),
            Constants.id("mastery/master"),
            Constants.id("mastery/starter"),
            ResourceLocation.withDefaultNamespace("end/root"),
            ResourceLocation.withDefaultNamespace("story/root"),
            ResourceLocation.fromNamespaceAndPath("pinferno", "agriculture/root"),
            ResourceLocation.fromNamespaceAndPath("pinferno", "combat/root"),
            ResourceLocation.fromNamespaceAndPath("pinferno", "exploration/root"),
            ResourceLocation.fromNamespaceAndPath("pinferno", "fishing/root"),
            ResourceLocation.fromNamespaceAndPath("pinferno", "nether/root")
    );

    @Inject(method = "award(Lnet/minecraft/advancements/AdvancementHolder;Ljava/lang/String;)Z", at = @At("RETURN"))
    private void preserved_blizzard$onAwardAdvancementCriterion(AdvancementHolder advancementHolder, String criterionKey, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            ServerPlayer serverPlayer = this.player;
            ResourceLocation advancementId = advancementHolder.id();
            MinecraftServer server = serverPlayer.getServer();

            if (EXCLUDED_ADVANCEMENTS.contains(advancementId) || advancementId.getPath().startsWith("recipes/")) {
                return;
            }

            Set<ResourceLocation> awardedAdvancements = WorldDataManager.getPlayerAwardedAdvancements(server, serverPlayer.getUUID());
            boolean wasNewAdvancement = awardedAdvancements.add(advancementId);

            if (wasNewAdvancement) {
                WorldDataManager.addPlayerAwardedAdvancement(server, serverPlayer.getUUID(), advancementId);
                Advancement advancement = advancementHolder.value();
                DisplayInfo display = advancement.display().orElse(null);

                if (display != null) {
                    AdvancementType type = display.getType();
                    int points = 0;

                    if (type == AdvancementType.TASK) {
                        points = 1;
                    }
                    else if (type == AdvancementType.GOAL) {
                        points = 3;
                    }
                    else if (type == AdvancementType.CHALLENGE) {
                        points = 7;
                    }

                    int currentPoints = WorldDataManager.getPlayerPoints(server, serverPlayer.getUUID());
                    WorldDataManager.setPlayerPoints(server, serverPlayer.getUUID(), currentPoints + points);
                    currentPoints = currentPoints + points;

                    UUID playerUUID = serverPlayer.getUUID();
                    ServerPlayNetworking.send(serverPlayer, new ModMessages.PlayerPointsPayload(playerUUID, currentPoints));

                    String oldRank = WorldDataManager.getPlayerRank(server, serverPlayer.getUUID());
                    String newRank = oldRank;

                    boolean hasAllNonExcludedAdvancements = true;

                    if (serverPlayer.getServer() != null) {
                        for (AdvancementHolder allAdvancementHolder : serverPlayer.getServer().getAdvancements().getAllAdvancements()) {
                            ResourceLocation currentAdvancementId = allAdvancementHolder.id();
                            if (!EXCLUDED_ADVANCEMENTS.contains(currentAdvancementId) && !currentAdvancementId.getPath().startsWith("recipes/")) {
                                if (!serverPlayer.getAdvancements().getOrStartProgress(allAdvancementHolder).isDone()) {
                                    hasAllNonExcludedAdvancements = false;
                                    break;
                                }
                            }
                        }
                    }

                    if (hasAllNonExcludedAdvancements) {
                        newRank = "infernal";
                    }
                    else {
                        if (currentPoints >= 240) {
                            newRank = "master";
                        }
                        else if (currentPoints >= 160) {
                            newRank = "advanced";
                        }
                        else if (currentPoints >= 100) {
                            newRank = "adequate";
                        }
                        else if (currentPoints >= 50) {
                            newRank = "intermediate";
                        }
                        else if (currentPoints >= 20) {
                            newRank = "beginner";
                        }
                        else if (currentPoints >= 6) {
                            newRank = "starter";
                        }
                    }

                    if (!newRank.equals(oldRank)) {
                        WorldDataManager.setPlayerRank(server, serverPlayer.getUUID(), newRank);
                        FabricModEvents.assignPlayerToRankTeam(serverPlayer);

                        if ("infernal".equals(newRank)) {
                            ModTriggers.MASTERY_INFERNAL.trigger(serverPlayer);
                        }
                        else {
                            switch (newRank) {
                                case "master": ModTriggers.MASTERY_MASTER.trigger(serverPlayer); break;
                                case "advanced": ModTriggers.MASTERY_ADVANCED.trigger(serverPlayer); break;
                                case "adequate": ModTriggers.MASTERY_ADEQUATE.trigger(serverPlayer); break;
                                case "intermediate": ModTriggers.MASTERY_INTERMEDIATE.trigger(serverPlayer); break;
                                case "beginner": ModTriggers.MASTERY_BEGINNER.trigger(serverPlayer); break;
                                case "starter": ModTriggers.MASTERY_STARTER.trigger(serverPlayer); break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }
}
