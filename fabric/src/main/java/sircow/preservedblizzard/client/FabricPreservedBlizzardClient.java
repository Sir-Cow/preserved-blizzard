package sircow.preservedblizzard.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import sircow.preservedblizzard.Constants;

public class FabricPreservedBlizzardClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // menus
        MenuScreens.register(Constants.PRESERVED_ENCHANT_MENU_TYPE.get(), PreservedEnchantingTableScreen::new);
    }
}
