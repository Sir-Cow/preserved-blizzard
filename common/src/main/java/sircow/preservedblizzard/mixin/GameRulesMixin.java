package sircow.preservedblizzard.mixin;

import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(GameRules.class)
public class GameRulesMixin {
    // set the playersSleepingPercentage gamerule to 0 by default
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=playersSleepingPercentage")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules$IntegerValue;create(I)Lnet/minecraft/world/level/GameRules$Type;", ordinal = 0))
    private static int sir_cow$changeVal(int p_46313_) {
        return 0;
    }
}
