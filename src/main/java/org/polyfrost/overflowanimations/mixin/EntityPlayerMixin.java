package org.polyfrost.overflowanimations.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.polyfrost.overflowanimations.config.OldAnimationsSettings;
import org.polyfrost.overflowanimations.hooks.SwingHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public class EntityPlayerMixin {

    @Inject(method = "dropItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;getEyeHeight()F"))
    public void overflowAnimations$dropItemSwing(ItemStack droppedItem, boolean dropAround, boolean traceItem, CallbackInfoReturnable<EntityItem> cir) {
        if (OldAnimationsSettings.modernDropSwing && OldAnimationsSettings.INSTANCE.enabled &&
                Minecraft.getMinecraft().theWorld.isRemote && Minecraft.getMinecraft().currentScreen instanceof GuiInventory) {
            SwingHook.swingItem();
        }
    }

}
