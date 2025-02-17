package sircow.preservedblizzard.mixin;

import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Attributes.class)
public class AttributesMixin {
    // change max value of armour points
    @ModifyArg(method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=armor")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/attributes/RangedAttribute;<init>(Ljava/lang/String;DDD)V"), index = 3)
    private static double preserved_blizzard$changeArmourMax(double maxValue) {
        return 150.0F;
    }
}
