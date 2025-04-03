package sircow.preservedblizzard.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import sircow.preservedblizzard.Constants;
import sircow.preservedblizzard.block.ModBlocks;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ModItems {
    public static final Item RHYOLITE = registerBlock(ModBlocks.RHYOLITE);
    public static final Item POLISHED_RHYOLITE = registerBlock(ModBlocks.POLISHED_RHYOLITE);
    public static final Item RHYOLITE_WALL = registerBlock(ModBlocks.RHYOLITE_WALL);
    public static final Item RHYOLITE_STAIRS = registerBlock(ModBlocks.RHYOLITE_STAIRS);
    public static final Item POLISHED_RHYOLITE_STAIRS = registerBlock(ModBlocks.POLISHED_RHYOLITE_STAIRS);
    public static final Item RHYOLITE_SLAB = registerBlock(ModBlocks.RHYOLITE_SLAB);
    public static final Item POLISHED_RHYOLITE_SLAB = registerBlock(ModBlocks.POLISHED_RHYOLITE_SLAB);

    private static ResourceKey<Item> blockIdToItemId(ResourceKey<Block> blockId) {
        return ResourceKey.create(Registries.ITEM, blockId.location());
    }

    private static ResourceKey<Item> moddedItemId(String name) {
        return ResourceKey.create(Registries.ITEM, Constants.id(name));
    }

    public static Item registerBlock(Block block) {
        return registerBlock(block, BlockItem::new);
    }

    public static Item registerBlock(Block block, BiFunction<Block, Item.Properties, Item> factory) {
        return registerBlock(block, factory, new Item.Properties());
    }

    public static Item registerBlock(Block block, BiFunction<Block, Item.Properties, Item> factory, Item.Properties properties) {
        return registerItem(
                blockIdToItemId(block.builtInRegistryHolder().key()), p_370785_ -> factory.apply(block, p_370785_), properties.useBlockDescriptionPrefix()
        );
    }

    public static Item registerItem(String name) {
        return registerItem(moddedItemId(name), Item::new, new Item.Properties());
    }

    public static Item registerItem(ResourceKey<Item> key, Function<Item.Properties, Item> factory, Item.Properties properties) {
        Item item = factory.apply(properties.setId(key));
        if (item instanceof BlockItem blockitem) {
            blockitem.registerBlocks(Item.BY_BLOCK, item);
        }

        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void registerModItems() {
        Constants.LOG.info("Registering Mod Items for " + Constants.MOD_ID);
    }
}
