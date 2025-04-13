package sircow.preservedblizzard.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import sircow.preservedblizzard.Constants;

import java.util.function.Function;

public class ModItems {

    private static ResourceKey<Item> moddedItemId(String name) {
        return ResourceKey.create(Registries.ITEM, Constants.id(name));
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
