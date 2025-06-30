package sircow.preservedblizzard.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedblizzard.client.ClientPointsManager;

@Mixin(AdvancementsScreen.class)
public class AdvancementsScreenMixin {
    @Inject(method = "renderWindow", at = @At("TAIL"))
    private void preserved_blizzard$renderPoints(GuiGraphics guiGraphics, int offsetX, int offsetY, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) {
            return;
        }

        int playerPoints = ClientPointsManager.getPlayerPoints(mc.player.getUUID());
        Component pointsText = Component.translatable("advancements.pblizzard.menu.mastery_points", playerPoints);

        guiGraphics.drawString(mc.font, pointsText, offsetX + 142, offsetY + 6, -12566464, false);
    }
}
