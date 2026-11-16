package com.elilouve.wingsclassic.client.render;

import com.elilouve.wingsclassic.config.ClientConfig;
import com.elilouve.wingsclassic.flight.Flight;
import com.elilouve.wingsclassic.flight.Flights;
import com.elilouve.wingsclassic.util.WingsMath;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;

/** Pose du corps du joueur en vol (appelée depuis les mixins). */
public final class FlightPose {
    private FlightPose() {}

    /** Bascule tout le corps à l'horizontale (équivalent de l'ancien ApplyRenderRotationsEvent.Post). */
    public static void applyBodyRotation(final LivingEntity entity, final PoseStack pose, final float delta) {
        final Flight flight = Flights.get(entity);
        if (flight == null || !ClientConfig.flightPose()) {
            return;
        }
        final float amt = flight.getFlyingAmount(delta);
        if (amt > 0.0F) {
            final float roll = bodyRoll(entity, delta);
            final float pitch = -WingsMath.lerpDegrees(entity.xRotO, entity.getXRot(), delta) - 90;
            pose.mulPose(Axis.ZP.rotationDegrees(WingsMath.lerpDegrees(0.0F, roll, amt)));
            pose.mulPose(Axis.XP.rotationDegrees(WingsMath.lerpDegrees(0.0F, pitch, amt)));
            pose.translate(0.0F, -1.2F * WingsMath.easeInOut(amt), 0.0F);
        }
    }

    /** Écart corps/tête, ramené dans [-180, 180] : sans cela, les tours accumulés faussent l'angle. */
    public static float bodyRoll(final LivingEntity entity, final float delta) {
        final float prev = net.minecraft.util.Mth.wrapDegrees(entity.yBodyRotO - entity.yRotO);
        final float now = net.minecraft.util.Mth.wrapDegrees(entity.yBodyRot - entity.getYRot());
        return net.minecraft.util.Mth.clamp(WingsMath.lerpDegrees(prev, now, delta), -90.0F, 90.0F);
    }

    /** Positions de la tête, des bras et des jambes en vol (équivalent de l'ancien PlayerModelEvent). */
    public static void applyLimbs(final HumanoidModel<?> model, final LivingEntity entity, final float ageInTicks, final float headPitch) {
        final Flight flight = Flights.get(entity);
        if (flight == null || !ClientConfig.flightPose()) {
            return;
        }
        final float delta = ageInTicks - entity.tickCount;
        final float amt = flight.getFlyingAmount(delta);
        if (amt > 0.0F) {
            model.head.xRot = WingsMath.toRadians(WingsMath.lerp(headPitch, headPitch / 4.0F - 90.0F, amt));
            model.leftArm.xRot = WingsMath.lerp(model.leftArm.xRot, -3.2F, amt);
            model.rightArm.xRot = WingsMath.lerp(model.rightArm.xRot, -3.2F, amt);
            model.leftLeg.xRot = WingsMath.lerp(model.leftLeg.xRot, 0.0F, amt);
            model.rightLeg.xRot = WingsMath.lerp(model.rightLeg.xRot, 0.0F, amt);
            model.hat.copyFrom(model.head);
        }
    }
}
