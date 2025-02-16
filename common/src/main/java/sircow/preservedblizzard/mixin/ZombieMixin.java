package sircow.preservedblizzard.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public class ZombieMixin {
    // stop villagers converting to zombies
    @Inject(at = @At("TAIL"), method = "convertVillagerToZombieVillager")
    private void preserved_blizzard$cancelZombieVillagerConvert(ServerLevel level, Villager villager, CallbackInfoReturnable<Boolean> cir) {
        cir.cancel();
    }
}
