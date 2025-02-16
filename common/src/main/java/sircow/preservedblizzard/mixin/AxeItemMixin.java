package sircow.preservedblizzard.mixin;

import net.minecraft.world.item.AxeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import sircow.preservedblizzard.RegisterItemChecker;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    // modify axe attackDamage
    @ModifyArg(method = "<init>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/DiggerItem;<init>(Lnet/minecraft/world/item/ToolMaterial;Lnet/minecraft/tags/TagKey;FFLnet/minecraft/world/item/Item$Properties;)V"),
            index = 2
    )
    private static float preserved_blizzard$modifyAttackDamage(float attackDamage) {
        if (RegisterItemChecker.flip) {
            if (RegisterItemChecker.AXES.contains(RegisterItemChecker.itemName)) {
                attackDamage = 4.0F;
            }
        }
        return attackDamage;
    }
    // modify axe attackSpeed
    @ModifyArg(method = "<init>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/DiggerItem;<init>(Lnet/minecraft/world/item/ToolMaterial;Lnet/minecraft/tags/TagKey;FFLnet/minecraft/world/item/Item$Properties;)V"),
            index = 3,
            remap = false
    )
    private static float preserved_blizzard$modifyAttackSpeed(float attackSpeed) {
        if (RegisterItemChecker.flip) {
            if (RegisterItemChecker.AXES.contains(RegisterItemChecker.itemName)) {
                attackSpeed = -3.0F;
            }
            RegisterItemChecker.flip = false;
        }
        return attackSpeed;
    }
}
