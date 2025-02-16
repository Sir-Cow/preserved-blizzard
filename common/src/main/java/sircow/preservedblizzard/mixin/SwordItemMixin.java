package sircow.preservedblizzard.mixin;

import net.minecraft.world.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import sircow.preservedblizzard.RegisterItemChecker;

@Mixin(SwordItem.class)
public class SwordItemMixin {
    // modify sword attackDamage
    @ModifyArg(method = "<init>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ToolMaterial;applySwordProperties(Lnet/minecraft/world/item/Item$Properties;FF)Lnet/minecraft/world/item/Item$Properties;"),
            index = 1
    )
    private static float preserved_blizzard$modifyAttackDamage(float attackDamage) {
        if (RegisterItemChecker.flip) {
            if (RegisterItemChecker.SWORDS.contains(RegisterItemChecker.itemName)) {
                attackDamage = 2.0F;
            }
            RegisterItemChecker.flip = false;
        }
        return attackDamage;
    }
}
