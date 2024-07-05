package org.polyfrost.overflowanimations.mixin.interfaces;

import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemRenderer.class)
public interface ItemRendererInvoker {

    @Accessor int getEquippedItemSlot();

}
