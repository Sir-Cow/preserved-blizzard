package sircow.preservedblizzard.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(ArmorMaterial.class)
public class ArmorMaterialMixin {
    @Final @Shadow private float knockbackResistance;
    @Final @Shadow private float toughness;
    @Final @Shadow private Map<ArmorType, Integer> defense;
    @Shadow @Final private ResourceKey<EquipmentAsset> assetId;

    @Unique private static final Map<ArmorType, Float> LEATHER_TOUGHNESS = new HashMap<>();
    @Unique private static final Map<ArmorType, Float> CHAINMAIL_TOUGHNESS = new HashMap<>();
    @Unique private static final Map<ArmorType, Float> IRON_TOUGHNESS = new HashMap<>();
    @Unique private static final Map<ArmorType, Float> GOLD_TOUGHNESS = new HashMap<>();
    @Unique private static final Map<ArmorType, Float> DIAMOND_TOUGHNESS = new HashMap<>();
    @Unique private static final Map<ArmorType, Float> NETHERITE_TOUGHNESS = new HashMap<>();

    static {
        LEATHER_TOUGHNESS.put(ArmorType.HELMET, 0.0f); LEATHER_TOUGHNESS.put(ArmorType.CHESTPLATE, 1.0f);
        LEATHER_TOUGHNESS.put(ArmorType.LEGGINGS, 1.0f); LEATHER_TOUGHNESS.put(ArmorType.BOOTS, 0.0f);
    }

    static {
        CHAINMAIL_TOUGHNESS.put(ArmorType.HELMET, 0.0f); CHAINMAIL_TOUGHNESS.put(ArmorType.CHESTPLATE, 2.0f);
        CHAINMAIL_TOUGHNESS.put(ArmorType.LEGGINGS, 1.0f); CHAINMAIL_TOUGHNESS.put(ArmorType.BOOTS, 0.0f);
    }

    static {
        IRON_TOUGHNESS.put(ArmorType.HELMET, 1.0f); IRON_TOUGHNESS.put(ArmorType.CHESTPLATE, 1.0f);
        IRON_TOUGHNESS.put(ArmorType.LEGGINGS, 1.0f); IRON_TOUGHNESS.put(ArmorType.BOOTS, 1.0f);
    }

    static {
        GOLD_TOUGHNESS.put(ArmorType.HELMET, 0.0f); GOLD_TOUGHNESS.put(ArmorType.CHESTPLATE, 2.0f);
        GOLD_TOUGHNESS.put(ArmorType.LEGGINGS, 1.0f); GOLD_TOUGHNESS.put(ArmorType.BOOTS, 0.0f);
    }

    static {
        DIAMOND_TOUGHNESS.put(ArmorType.HELMET, 1.0f); DIAMOND_TOUGHNESS.put(ArmorType.CHESTPLATE, 2.0f);
        DIAMOND_TOUGHNESS.put(ArmorType.LEGGINGS, 2.0f); DIAMOND_TOUGHNESS.put(ArmorType.BOOTS, 1.0f);
    }

    static {
        NETHERITE_TOUGHNESS.put(ArmorType.HELMET, 2.0f); NETHERITE_TOUGHNESS.put(ArmorType.CHESTPLATE, 2.0f);
        NETHERITE_TOUGHNESS.put(ArmorType.LEGGINGS, 2.0f); NETHERITE_TOUGHNESS.put(ArmorType.BOOTS, 2.0f);
    }

    // modify toughness values for individual armour pieces
    @Inject(method = "createAttributes", at = @At("HEAD"), cancellable = true)
    private void thing(ArmorType armorType, CallbackInfoReturnable<ItemAttributeModifiers> cir) {
        int defenseVal = this.defense.getOrDefault(armorType, 0);
        float toughVal;
        ItemAttributeModifiers.Builder itemattributemodifiers$builder = ItemAttributeModifiers.builder();
        EquipmentSlotGroup equipmentslotgroup = EquipmentSlotGroup.bySlot(armorType.getSlot());
        ResourceLocation resourcelocation = ResourceLocation.withDefaultNamespace("armor." + armorType.getName());
        itemattributemodifiers$builder.add(Attributes.ARMOR, new AttributeModifier(resourcelocation, defenseVal, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup);

        if (assetId.toString().contains("leather")) {
            toughVal = LEATHER_TOUGHNESS.getOrDefault(armorType, 0.0F);
        }
        else if (assetId.toString().contains("chainmail")) {
            toughVal = CHAINMAIL_TOUGHNESS.getOrDefault(armorType, 0.0F);
        }
        else if (assetId.toString().contains("iron")) {
            toughVal = IRON_TOUGHNESS.getOrDefault(armorType, 0.0F);
        }
        else if (assetId.toString().contains("gold")) {
            toughVal = GOLD_TOUGHNESS.getOrDefault(armorType, 0.0F);
        }
        else if (assetId.toString().contains("diamond")) {
            toughVal = DIAMOND_TOUGHNESS.getOrDefault(armorType, 0.0F);
        }
        else if (assetId.toString().contains("netherite")) {
            toughVal = NETHERITE_TOUGHNESS.getOrDefault(armorType, 0.0F);
        }
        else {
            toughVal = this.toughness;
        }

        itemattributemodifiers$builder.add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(resourcelocation, toughVal, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup);
        if (this.knockbackResistance > 0.0F) {
            itemattributemodifiers$builder.add(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(resourcelocation, this.knockbackResistance, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup);
        }

        cir.setReturnValue(itemattributemodifiers$builder.build());
        cir.cancel();
    }
}
