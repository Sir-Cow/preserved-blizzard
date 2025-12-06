package sircow.preservedblizzard.mixin;

import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(GameRules.class)
public class GameRulesMixin {
    // doImmediateRespawn true by default
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=doImmediateRespawn")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules$BooleanValue;create(ZLjava/util/function/BiConsumer;)Lnet/minecraft/world/level/GameRules$Type;", ordinal = 0))
    private static boolean preserved_blizzard$changeVal(boolean val) {
        return true;
    }
}
