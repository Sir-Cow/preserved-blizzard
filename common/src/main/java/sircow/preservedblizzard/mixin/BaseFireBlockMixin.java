package sircow.preservedblizzard.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;

@Mixin(BaseFireBlock.class)
public class BaseFireBlockMixin {
    @ModifyVariable(method = "isPortal", at = @At(value = "INVOKE", target="Lnet/minecraft/core/Direction;values()[Lnet/minecraft/core/Direction;"))
    private static boolean sir_cow$portalCheck(boolean bl, Level level, BlockPos pos, Direction p_direction) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();

        for (Direction direction : Direction.values()) {
            if (level.getBlockState(blockpos$mutableblockpos.set(pos).move(direction)).is(Blocks.OBSIDIAN)
                    || level.getBlockState(blockpos$mutableblockpos.set(pos).move(direction)).is(Blocks.CRYING_OBSIDIAN)) {
                return true;
            }
        }
        return false;
    }
}
