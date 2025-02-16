package sircow.preservedblizzard.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import sircow.preservedblizzard.RegisterItemChecker;

@Mixin(Items.class)
public class ItemsMixin {
    // catch item names
    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;)Lnet/minecraft/world/item/Item;"
            ),
            index = 0
    )
    private static String preserved_blizzard$catchItemName(String name) {
        if (RegisterItemChecker.SWORDS.contains(name) || RegisterItemChecker.AXES.contains(name)) {
            preserved_blizzard$callFlip(name);
        }
        return name;
    }

    @Unique
    private static void preserved_blizzard$callFlip(String itemName) {
        RegisterItemChecker.flip = true;
        RegisterItemChecker.itemName = itemName;
    }

    // make trident repairable
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=trident")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_blizzard$modifyTrident(Item.Properties properties) {
        return new Item.Properties()
                .rarity(Rarity.RARE)
                .durability(250)
                .attributes(TridentItem.createAttributes())
                .component(DataComponents.TOOL, TridentItem.createToolProperties())
                .enchantable(1)
                .repairable(Items.PRISMARINE_CRYSTALS);
    }
}
