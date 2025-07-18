package sircow.preservedblizzard.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.preservedblizzard.client.ClientPointsManager;

@Pseudo
@Mixin(targets = "betteradvancements.common.gui.BetterAdvancementsScreen")
public class BetterAdvancementsScreenMixin {
    @Dynamic
    @Inject(method = "render", at = @At("TAIL"))
    private void preserved_blizzard$renderPoints(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        int playerPoints = ClientPointsManager.getPlayerPoints(mc.player.getUUID());
        Component pointsText = Component.translatable("advancements.pblizzard.menu.mastery_points", playerPoints);

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int internalWidth = screenWidth * 100 / 100;
        int internalHeight = screenHeight * 100 / 100;
        int SIDE = 30;
        int TOP = 40;
        int left = SIDE + (screenWidth - internalWidth) / 2;
        int top = TOP + (screenHeight - internalHeight) / 2;
        int right = internalWidth - SIDE + (screenWidth - internalWidth) / 2;
        int textWidth = mc.font.width(pointsText);
        int drawX = right - textWidth - 8;
        int drawY = top + 6;

        guiGraphics.drawString(mc.font, pointsText, drawX, drawY, -12566464, false);
    }
}
