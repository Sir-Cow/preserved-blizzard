package sircow.preservedblizzard;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.MenuType;
import sircow.preservedblizzard.other.FabricModEvents;
import sircow.preservedblizzard.screen.PreservedEnchantmentMenu;

public class PreservedBlizzard implements ModInitializer {
    // menus
    private static final MenuType<PreservedEnchantmentMenu> PRESERVED_ENCHANT_MENU_TYPE =
            Registry.register(BuiltInRegistries.MENU, Constants.id("preserved_enchant"),
                    new ExtendedScreenHandlerType<>((pWindowID, pInventory, pData) -> new PreservedEnchantmentMenu(pWindowID, pInventory), BlockData.CODEC));

    // codecs
    public record BlockData(boolean empty) {
        public static final StreamCodec<RegistryFriendlyByteBuf, BlockData> CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL,
                BlockData::empty,
                BlockData::new
        );
    }

    static {
        Constants.PRESERVED_ENCHANT_MENU_TYPE = () -> PRESERVED_ENCHANT_MENU_TYPE;
    }

    @Override
    public void onInitialize() {
        CommonClass.init();
        FabricModEvents.registerModEvents();
    }
}
