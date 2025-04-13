package sircow.preservedblizzard;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sircow.preservedblizzard.screen.PreservedEnchantmentMenu;

import java.util.function.Supplier;

public class Constants {
	public static final String MOD_ID = "pblizzard";
	public static final String MOD_NAME = "Preserved: Blizzard";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static Supplier<MenuType<PreservedEnchantmentMenu>> PRESERVED_ENCHANT_MENU_TYPE;

	public static ResourceLocation id(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}
}
