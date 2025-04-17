package sircow.preservedblizzard.mixin;

import net.minecraft.world.item.CrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CrossbowItem.class)
public class CrossbowMixin {
    // modify crossbow draw speed
    @ModifyVariable(method = "getChargeDuration", at = @At("STORE"), ordinal = 0)
    private static float preserved_blizzard$modifyDrawSpd(float originalValue) {
        return 1.0F;
    }
}
