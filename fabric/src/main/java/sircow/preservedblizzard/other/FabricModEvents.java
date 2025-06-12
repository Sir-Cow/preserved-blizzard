package sircow.preservedblizzard.other;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import sircow.preservedblizzard.Constants;
import sircow.preservedblizzard.effect.ModEffects;

public class FabricModEvents {
    public static void removeTridentDropFromDrowned() {
        ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, damageSource) -> {
            if (livingEntity instanceof Drowned drowned) {
                ItemStack heldItem = drowned.getItemInHand(InteractionHand.MAIN_HAND);
                if (heldItem.getItem() instanceof TridentItem) {
                    drowned.setDropChance(EquipmentSlot.MAINHAND, 0);
                }
            }
        });
    }

    public static void handleEntityDeath() {
        ServerPlayerEvents.ALLOW_DEATH.register((player, damageSource, damageAmount) -> {
            TempInventoryStorage.savePlayerInventory(player);
            return true;
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            boolean hadWellRestedEffectOnDeath = TempInventoryStorage.restorePlayerInventory(newPlayer);

            // display message if player had well rested effect
            if (hadWellRestedEffectOnDeath) {
                newPlayer.server.execute(() -> newPlayer.sendSystemMessage(Component.translatable("effect.pblizzard.well_rested_consume"), true));
            }
        });
    }

    public static void modifySleeping() {
        EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
            if (entity instanceof Player player) {
                if (player.getSleepTimer() > 20 && !player.level().isMoonVisible()) {
                    MinecraftServer server = player.level().getServer();
                    if (server != null) {
                        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
                            serverPlayer.addEffect(new MobEffectInstance(ModEffects.WELL_RESTED, 24000, 0, false, false, true));
                            serverPlayer.displayClientMessage(Component.translatable("effect.pblizzard.well_rested_awake"), true);
                        }
                    }
                }
            }
        });
    }

    public static void registerModEvents() {
        Constants.LOG.info("Registering Fabric Mod Events for " + Constants.MOD_ID);
        removeTridentDropFromDrowned();
        handleEntityDeath();
        modifySleeping();
    }
}
