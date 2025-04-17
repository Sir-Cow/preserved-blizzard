package sircow.preservedblizzard.other;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import sircow.preservedblizzard.Constants;

public class FabricModEvents {
    public static void removeTridentDropFromDrowned() {
        ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, damageSource) -> {
            if (livingEntity instanceof Drowned drowned) {
                ItemStack heldItem = drowned.getItemInHand(InteractionHand.MAIN_HAND);
                if (heldItem.getItem() instanceof TridentItem) {
                    ((Drowned) livingEntity).setDropChance(EquipmentSlot.MAINHAND, 0);
                }
            }
        });
    }

    public static void registerModEvents() {
        Constants.LOG.info("Registering Fabric Mod Events for " + Constants.MOD_ID);
        removeTridentDropFromDrowned();
    }
}
