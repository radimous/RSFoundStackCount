package com.radimous.rsfoundstackcount.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.grid.GridScreen;
import com.refinedmods.refinedstorage.screen.grid.view.IGridView;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GridScreen.class, remap = false)
public abstract class GridScreenMixin extends BaseScreen {
    @Shadow
    private IGridView view;

    protected GridScreenMixin(AbstractContainerMenu containerMenu, int xSize, int ySize, Inventory inventory, Component title) {
        super(containerMenu, xSize, ySize, inventory, title);
    }

    @Inject(method = "renderForeground", at = @At("HEAD"))
    private void renderNumberOfFoundStacks(PoseStack poseStack, int mouseX, int mouseY, CallbackInfo ci) {
        String formattedQty = API.instance().getQuantityFormatter().formatWithUnits(this.view.getStacks().size());
        poseStack.pushPose();
        float scale = 0.75F;
        int formattedWidth = Math.round(this.font.width(formattedQty) * scale);
        poseStack.scale(scale, scale, scale);
        this.renderString(poseStack, (this.imageWidth + 11 - (formattedWidth/2)), 10, formattedQty);
        poseStack.popPose();
    }
}

