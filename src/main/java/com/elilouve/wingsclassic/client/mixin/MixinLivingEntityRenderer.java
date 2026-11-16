package com.elilouve.wingsclassic.client.mixin;

import com.elilouve.wingsclassic.client.render.FlightPose;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> {
    @Inject(method = "setupRotations", at = @At("TAIL"))
    private void wingsclassic$applyFlightRotation(final T entity, final PoseStack pose, final float bob, final float yBodyRot, final float partialTick, final float scale, final CallbackInfo ci) {
        FlightPose.applyBodyRotation(entity, pose, partialTick);
    }
}
