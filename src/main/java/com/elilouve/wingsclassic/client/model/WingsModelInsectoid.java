package com.elilouve.wingsclassic.client.model;

import com.elilouve.wingsclassic.client.anim.AnimatorInsectoid;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.FastColor;

public final class WingsModelInsectoid extends WingsModel<AnimatorInsectoid> {
    private final Bone root = new Bone();
    private final Bone wingLeft = new Bone().at(0, 2, 3.5F).box(0, 0, 0, -8, 0, 19, 24, 0);
    private final Bone wingRight = new Bone().at(0, 2, 3.5F).box(0, 24, -19, -8, 0, 19, 24, 0);

    public WingsModelInsectoid() {
        this.root.add(this.wingLeft).add(this.wingRight);
    }

    @Override
    public void render(final AnimatorInsectoid animator, final float delta, final PoseStack pose, final VertexConsumer buf, final int light, final int overlay, final float flap, final float alpha) {
        setAngles(this.wingLeft.part, this.wingRight.part, animator.getRotation(delta).add(0.0D, flap * 0.8D, 0.0D));
        this.root.part.render(pose, buf, light, overlay, FastColor.ARGB32.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F));
    }
}
