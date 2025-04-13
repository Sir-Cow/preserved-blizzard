package sircow.preservedblizzard.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
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
                    .strength(1.5F, 6.0F), true);
    public static final Block POLISHED_RHYOLITE = register("polished_rhyolite",
            Block::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F), true);
    public static final Block RHYOLITE_STAIRS = registerStair("rhyolite_stairs", RHYOLITE);
    public static final Block POLISHED_RHYOLITE_STAIRS = registerStair("polished_rhyolite_stairs", POLISHED_RHYOLITE);
    public static final Block RHYOLITE_SLAB = register("rhyolite_slab", SlabBlock::new, BlockBehaviour.Properties.ofFullCopy(RHYOLITE), true);
    public static final Block POLISHED_RHYOLITE_SLAB = register("polished_rhyolite_slab", SlabBlock::new, BlockBehaviour.Properties.ofFullCopy(POLISHED_RHYOLITE), true);
    public static final Block RHYOLITE_WALL = register("rhyolite_wall", WallBlock::new, BlockBehaviour.Properties.ofFullCopy(RHYOLITE).forceSolidOn(), true);

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
        ResourceKey<Block> blockKey = keyOfBlock(name);
        Block block = blockFactory.apply(settings.setId(blockKey));

        if (shouldRegisterItem) {
            ResourceKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey));
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, Constants.id(name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, Constants.id(name));
    }

    private static Block registerStair(String name, Block baseBlock) {
        BlockState baseBlockState = baseBlock.defaultBlockState();
        return register(name, (properties) -> new PreservedStairBlock(baseBlockState, properties), BlockBehaviour.Properties.ofFullCopy(baseBlock), true);
    }

    public static void registerModBlocks() {
        Constants.LOG.info("Registering Mod Blocks for " + Constants.MOD_ID);
    }
}
