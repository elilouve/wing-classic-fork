package com.elilouve.wingsclassic.client.render;

import com.elilouve.wingsclassic.client.view.FlightView;
import com.elilouve.wingsclassic.config.ClientConfig;
import com.elilouve.wingsclassic.client.view.FlightViews;
import com.elilouve.wingsclassic.config.ClientConfig;
import com.elilouve.wingsclassic.flight.Flight;
import com.elilouve.wingsclassic.flight.Flights;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import com.elilouve.wingsclassic.flight.WingsHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/** Couche de rendu qui dessine les ailes dans le dos de l'entité. */
public final class WingsLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
    public WingsLayer(final RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(final PoseStack pose, final MultiBufferSource buffers, final int light, final T entity, final float limbSwing, final float limbSwingAmount, final float partialTick, final float ageInTicks, final float netHeadYaw, final float headPitch) {
        if (!ClientConfig.renderWings() || entity.isInvisible()) {
            return;
        }
        final ItemStack stack = WingsHelper.findWings(entity);
        if (stack.isEmpty()) {
            return;
        }
        final FlightView view = FlightViews.get(entity);
        final Flight flight = Flights.get(entity);
        final float deploy = flight == null ? 0.0F : flight.getFlyingAmount(partialTick);
        view.ifFormPresent(form -> {
            pose.pushPose();
            if (entity.isCrouching()) {
                pose.translate(0.0F, 0.2F, 0.0F);
            }
            this.getParentModel().body.translateAndRotate(pose);
            final boolean glow = form.fire() && ClientConfig.fireGlow() && deploy > 0.0F;
            // Ailes de feu : plus elles se déploient, plus elles s'embrasent (lumière + halo).
            final int lit = glow
                ? LightTexture.pack(Math.max(LightTexture.block(light), Math.round(15.0F * deploy)), LightTexture.sky(light))
                : light;
            final VertexConsumer buf = ItemRenderer.getArmorFoilBuffer(buffers, RenderType.entityCutoutNoCull(form.texture()), stack.hasFoil());
            form.render(partialTick, pose, buf, lit, OverlayTexture.NO_OVERLAY, 1.0F);
            if (glow) {
                final float flicker = 0.85F + 0.15F * Mth.sin((entity.tickCount + partialTick) * 0.9F);
                final VertexConsumer halo = buffers.getBuffer(RenderType.entityTranslucentEmissive(form.texture()));
                form.render(partialTick, pose, halo, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0.55F * deploy * flicker);
            }
            pose.popPose();
        });
    }
}
