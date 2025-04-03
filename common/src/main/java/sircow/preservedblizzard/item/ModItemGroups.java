package sircow.preservedblizzard.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import sircow.preservedblizzard.Constants;
import sircow.preservedblizzard.block.ModBlocks;

public class ModItemGroups {
    public static final ResourceKey<CreativeModeTab> PRESERVED_BLIZZARD_TAB_KEY =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, Constants.id("pblizzard"));

    public static CreativeModeTab PRESERVED_BLIZZARD_GROUP;

    public static void register() {
        PRESERVED_BLIZZARD_GROUP = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(Component.translatable("itemgroup.pblizzard.items"))
                .icon(() -> new ItemStack(ModBlocks.RHYOLITE))
                .displayItems((displayContext, entries) -> {
                    entries.accept(ModItems.RHYOLITE);
                    entries.accept(ModItems.POLISHED_RHYOLITE);
                    entries.accept(ModItems.RHYOLITE_WALL);
                    entries.accept(ModItems.RHYOLITE_STAIRS);
                    entries.accept(ModItems.POLISHED_RHYOLITE_STAIRS);
                    entries.accept(ModItems.RHYOLITE_SLAB);
                    entries.accept(ModItems.POLISHED_RHYOLITE_SLAB);
                })
                .build();
        registerCreativeTab(PRESERVED_BLIZZARD_GROUP);
    }

    private static void registerCreativeTab(CreativeModeTab tab){
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ModItemGroups.PRESERVED_BLIZZARD_TAB_KEY, tab);
    }

    public static void registerItemGroups() {
        register();
        Constants.LOG.info("Registering Mod Item Groups for " + Constants.MOD_ID);
    }
}
