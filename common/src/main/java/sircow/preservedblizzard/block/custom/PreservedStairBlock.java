package sircow.preservedblizzard.block.custom;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

public class PreservedStairBlock extends StairBlock {
    // made because StairBlock has protected access
    public PreservedStairBlock(BlockState baseState, Properties properties) {
        super(baseState, properties);
    }
}
