package sircow.preservedblizzard.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Inject(method = "addEntity", at = @At("HEAD"))
    private void preserved_blizzard$cancelZombieVillagerSpawning(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity.getType() == EntityType.ZOMBIE_VILLAGER) {
            entity.setRemoved(Entity.RemovalReason.DISCARDED);
        }
    }
}
