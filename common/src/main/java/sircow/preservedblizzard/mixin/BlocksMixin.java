package sircow.preservedblizzard.mixin;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public class BlocksMixin {
    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=trial_spawner")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;"),
            index = 2
    )
    private static BlockBehaviour.Properties preserved_blizzard$modifyTrialSpawner(BlockBehaviour.Properties properties) {
        return properties.strength(-1F);
    }
}
