package org.polyfrost.overflowanimations.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.GuiIngameForge;
import org.objectweb.asm.Opcodes;
import org.polyfrost.overflowanimations.config.OldAnimationsSettings;
import org.polyfrost.overflowanimations.hooks.DebugCrosshairHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameForge.class)
public class GuiIngameForgeMixin extends GuiIngame {

    @Shadow(remap = false) private ScaledResolution res;

    public GuiIngameForgeMixin(Minecraft minecraft) {
        super(minecraft);
    }

    @Redirect(method = "renderHUDText", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/GuiIngameForge;drawRect(IIIII)V"))
    private void overflowAnimations$cancelBackgroundDrawing(int left, int top, int right, int bottom, int color) {
        if (!OldAnimationsSettings.INSTANCE.enabled || !(OldAnimationsSettings.INSTANCE.debugScreenMode == 0 ||
                OldAnimationsSettings.INSTANCE.debugScreenMode == 2)) {
            GuiIngameForge.drawRect(left, top, right, bottom, color);
        }
    }

    @Redirect(method = "renderHUDText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I"))
    private int overflowAnimations$removeShadow(FontRenderer fontRenderer, String text, int x, int y, int color) {
        return fontRenderer.drawString(text, x, y, color, (OldAnimationsSettings.INSTANCE.debugScreenMode == 0 || OldAnimationsSettings.INSTANCE.debugScreenMode == 2)
                && OldAnimationsSettings.INSTANCE.enabled);
    }

    @ModifyVariable(method = "renderHealth", at = @At(value = "LOAD", ordinal = 1), index = 5, remap = false)
    private boolean overflowAnimations$cancelFlash(boolean original) {
        return (!OldAnimationsSettings.oldHealth || !OldAnimationsSettings.INSTANCE.enabled) && original;
    }

}
