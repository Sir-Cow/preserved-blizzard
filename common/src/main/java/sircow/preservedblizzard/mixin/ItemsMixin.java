package sircow.preservedblizzard.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
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
        if (RegisterItemChecker.AXES.contains(name)) {
            preserved_blizzard$callFlip(name);
        }
        return name;
    }

    @Unique
    private static void preserved_blizzard$callFlip(String itemName) {
        RegisterItemChecker.flip = true;
        RegisterItemChecker.itemName = itemName;
    }

    // modify sword attack damage
    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=wooden_sword")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_blizzard$modifyWoodenSword(Item.Properties properties) {
        return new Item.Properties()
                .sword(ToolMaterial.WOOD, 2.0F, -2.4F);
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=golden_sword")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_blizzard$modifyGoldenSword(Item.Properties properties) {
        return new Item.Properties()
                .sword(ToolMaterial.GOLD, 2.0F, -2.4F);
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=iron_sword")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_blizzard$modifyIronSword(Item.Properties properties) {
        return new Item.Properties()
                .sword(ToolMaterial.IRON, 2.0F, -2.4F);
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=stone_sword")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_blizzard$modifyStoneSword(Item.Properties properties) {
        return new Item.Properties()
                .sword(ToolMaterial.STONE, 2.0F, -2.4F);
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=diamond_sword")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_blizzard$modifyDiamondSword(Item.Properties properties) {
        return new Item.Properties()
                .sword(ToolMaterial.DIAMOND, 2.0F, -2.4F);
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=netherite_sword")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_blizzard$modifyNetheriteSword(Item.Properties properties) {
        return new Item.Properties()
                .sword(ToolMaterial.NETHERITE, 2.0F, -2.4F).fireResistant();
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
