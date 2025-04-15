package sircow.preservedblizzard.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public class ZombieMixin {
    // stop villagers converting to zombies
    @Inject(at = @At("RETURN"), method = "convertVillagerToZombieVillager", cancellable = true)
    private void preserved_blizzard$cancelZombieVillagerConvert(ServerLevel level, Villager villager, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    // modify armour value
    @ModifyArg(
            method = "createAttributes",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;add(Lnet/minecraft/core/Holder;D)Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;",
                    ordinal = 3),
            index = 1
    )
    private static double preserved_blizzard$modifyDamage(double baseValue) {
        baseValue = 10.0F;
        return baseValue;
    }
}
