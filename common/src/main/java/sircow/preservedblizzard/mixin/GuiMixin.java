package sircow.preservedblizzard.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Unique private static final ResourceLocation NEW_ARMOUR_BAR_EMPTY = ResourceLocation.fromNamespaceAndPath("preservedblizzard", "hud/armor_bar_empty");
    @Unique private static final ResourceLocation NEW_ARMOUR_BAR_FILLED = ResourceLocation.fromNamespaceAndPath("preservedblizzard", "hud/armor_bar_filled");

    // replace vanilla with custom armour bar
    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private static void preserved_blizzard$modifyArmourBar(GuiGraphics guiGraphics, Player player, int y, int heartRows, int height, int x, CallbackInfo ci) {
        double maxArmourVal = 100.0F; // its actually 150 but this is a cap for only the bar
        int armourVal = player.getArmorValue();
        double percentageMultiplier;
        int barWidth = 81;
        int barHeight = 9;

        int j = y - (heartRows - 1) * height - 10;

        if (armourVal > 0) {
            guiGraphics.blitSprite(RenderType::guiTextured, NEW_ARMOUR_BAR_EMPTY, x, j, barWidth, barHeight);

            if (armourVal >= 100) {
                percentageMultiplier = 1.0F;
            }
            else {
                percentageMultiplier = armourVal / maxArmourVal;
            }

            guiGraphics.blitSprite(RenderType::guiTextured, NEW_ARMOUR_BAR_FILLED, barWidth, barHeight, 0, 0, x, j, (int)(percentageMultiplier * barWidth), barHeight);
        }
        ci.cancel();
    }
}
