package sircow.preservedblizzard.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import sircow.preservedblizzard.RegisterItemChecker;

@Mixin(Items.class)
public class ItemsMixin {
    // catch item names
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;)Lnet/minecraft/world/item/Item;"), index = 0)
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
    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=wooden_sword"),
            to = @At(value = "CONSTANT", args = "stringValue=stone_sword")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item$Properties;sword(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;"),
            index = 1
    )
    private static float preserved_blizzard$modifyWoodenSword(float attackDamage) {
        return 2.0F;
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=golden_sword"),
            to = @At(value = "CONSTANT", args = "stringValue=iron_sword")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item$Properties;sword(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;"),
            index = 1
    )
    private static float preserved_blizzard$modifyGoldenSword(float attackDamage) {
        return 2.0F;
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=iron_sword"),
            to = @At(value = "CONSTANT", args = "stringValue=diamond_sword")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item$Properties;sword(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;"),
            index = 1
    )
    private static float preserved_blizzard$modifyIronSword(float attackDamage) {
        return 2.0F;
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=stone_sword"),
            to = @At(value = "CONSTANT", args = "stringValue=golden_sword")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item$Properties;sword(Lnet/minecraft/world/item/ToolMaterial;FF)Lnet/minecraft/world/item/Item$Properties;"),
            index = 1
    )
    private static float preserved_blizzard$modifyStoneSword(float attackDamage) {
        return 2.0F;
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=trident")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_blizzard$modifyTrident(Item.Properties properties) {
        return new Item.Properties()
                .rarity(Rarity.EPIC)
                .durability(2031)
                .attributes(TridentItem.createAttributes())
                .component(DataComponents.TOOL, TridentItem.createToolProperties())
                .enchantable(1)
                .repairable(Items.PRISMARINE_CRYSTALS);
    }

    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=mace")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_blizzard$modifyMace(Item.Properties properties) {
        return new Item.Properties()
                .rarity(Rarity.EPIC)
                .durability(2031)
                .component(DataComponents.TOOL, MaceItem.createToolProperties())
                .repairable(Items.BREEZE_ROD)
                .attributes(MaceItem.createAttributes())
                .enchantable(15)
                .component(DataComponents.WEAPON, new Weapon(1));
    }

    // modify bow durability
    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=bow")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_blizzard$modifyBow(Item.Properties properties) {
        return new Item.Properties().durability(465).enchantable(1);
    }

    // modify chainmail rarity
    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=chainmail_helmet")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_blizzard$modifyChainmail1(Item.Properties properties) {
        return new Item.Properties().humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.HELMET).rarity(Rarity.COMMON);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=chainmail_chestplate")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_blizzard$modifyChainmail2(Item.Properties properties) {
        return new Item.Properties().humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.CHESTPLATE).rarity(Rarity.COMMON);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=chainmail_leggings")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_blizzard$modifyChainmail3(Item.Properties properties) {
        return new Item.Properties().humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.LEGGINGS).rarity(Rarity.COMMON);
    }
    @ModifyArg(method = "<clinit>", slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=chainmail_boots")), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;",
            ordinal = 0
    ))
    private static Item.Properties preserved_blizzard$modifyChainmail4(Item.Properties properties) {
        return new Item.Properties().humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.BOOTS).rarity(Rarity.COMMON);
    }
}
