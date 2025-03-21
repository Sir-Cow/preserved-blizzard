package sircow.preservedblizzard.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import sircow.preservedblizzard.Constants;
import sircow.preservedblizzard.block.custom.PreservedStairBlock;

import java.util.function.Function;

public class ModBlocks {
    public static final Block RHYOLITE = register("rhyolite",
            Block::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F));
    public static final Block POLISHED_RHYOLITE = register("polished_rhyolite",
            Block::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F));
    public static final Block RHYOLITE_STAIRS = registerStair("rhyolite_stairs", RHYOLITE);
    public static final Block POLISHED_RHYOLITE_STAIRS = registerStair("polished_rhyolite_stairs", POLISHED_RHYOLITE);
    public static final Block RHYOLITE_SLAB = register("rhyolite_slab", SlabBlock::new, BlockBehaviour.Properties.ofFullCopy(RHYOLITE));
    public static final Block POLISHED_RHYOLITE_SLAB = register("polished_rhyolite_slab", SlabBlock::new, BlockBehaviour.Properties.ofFullCopy(POLISHED_RHYOLITE));
    public static final Block RHYOLITE_WALL = register("rhyolite_wall", WallBlock::new, BlockBehaviour.Properties.ofFullCopy(RHYOLITE).forceSolidOn());

    private static Block register(ResourceKey<Block> resourceKey, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        Block block = factory.apply(properties.setId(resourceKey));
        return Registry.register(BuiltInRegistries.BLOCK, resourceKey, block);
    }

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        return register(keyOf(name), factory, properties);
    }

    private static ResourceKey<Block> keyOf(String name) {
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }

    private static Block registerStair(String name, Block baseBlock) {
        BlockState baseBlockState = baseBlock.defaultBlockState();
        return register(name, (properties) -> new PreservedStairBlock(baseBlockState, properties), BlockBehaviour.Properties.ofFullCopy(baseBlock));
    }

    public static void registerModBlocks() {
        Constants.LOG.info("Registering Mod Blocks for " + Constants.MOD_ID);
    }
}
