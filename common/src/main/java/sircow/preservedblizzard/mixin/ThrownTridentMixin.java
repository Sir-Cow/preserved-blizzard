package sircow.preservedblizzard.mixin;

import net.minecraft.world.entity.projectile.ThrownTrident;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ThrownTrident.class)
public class ThrownTridentMixin {
    // modify thrown trident damage
    @ModifyVariable(method = "onHitEntity", at = @At("STORE"), ordinal = 0)
    private float preserved_blizzard$modifyDamage(float originalValue) {
        return 9.0F;
    }
}
