package sircow.preservedblizzard.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import sircow.preservedblizzard.Constants;

import java.util.function.Function;

public class ModItems {

    public static Item registerItem(String name, Function<Item.Properties, Item> itemFactory, Item.Properties properties) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Constants.id(name));
        Item item = itemFactory.apply(properties.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static void registerModItems() {
        Constants.LOG.info("Registering Mod Items for " + Constants.MOD_ID);
    }
}
