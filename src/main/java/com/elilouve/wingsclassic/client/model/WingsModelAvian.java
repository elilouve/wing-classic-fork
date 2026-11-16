package com.elilouve.wingsclassic.client.model;

import com.elilouve.wingsclassic.client.anim.AnimatorAvian;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.FastColor;
import net.minecraft.client.model.geom.ModelPart;

import java.util.List;

public final class WingsModelAvian extends WingsModel<AnimatorAvian> {
    private static final float[] FLAP_WEIGHT = {1.0F, 0.6F, 0.4F, 0.2F};
    private final Bone root = new Bone();
    private final List<ModelPart> bonesLeft, bonesRight, feathersLeft, feathersRight;

    public WingsModelAvian() {
        final Bone coracoidLeft = new Bone().at(1.5F, 5.5F, 2.5F).box(0, 28, 0, -1.5F, -1.5F, 5, 3, 3);
        final Bone coracoidRight = new Bone().at(-1.5F, 5.5F, 2.5F).box(0, 34, -5, -1.5F, -1.5F, 5, 3, 3);
        final Bone humerusLeft = new Bone().at(4.7F, -0.6F, 0.1F).box(0, 0, -0.1F, -1.1F, -2, 7, 3, 4);
        final Bone humerusRight = new Bone().at(-4.7F, -0.6F, 0.1F).box(0, 7, -6.9F, -1.1F, -2, 7, 3, 4);
        final Bone ulnaLeft = new Bone().at(6.5F, 0.2F, 0.1F).box(22, 0, 0, -1.5F, -1.5F, 9, 3, 3);
        final Bone ulnaRight = new Bone().at(-6.5F, 0.2F, 0.1F).box(22, 6, -9, -1.5F, -1.5F, 9, 3, 3);
        final Bone carpalsLeft = new Bone().at(8.5F, 0, 0).box(46, 0, 0, -1, -1, 5, 2, 2);
        final Bone carpalsRight = new Bone().at(-8.5F, 0, 0).box(46, 4, -5, -1, -1, 5, 2, 2);
        final Bone feathersCoracoidLeft = new Bone().at(0.4F, 0, 1).feather(6, 40, 0, 0, -1, 6, 8);
        final Bone feathersCoracoidRight = new Bone().at(-0.4F, 0, 1).feather(0, 40, -6, 0, -1, 6, 8);
        final Bone feathersTertiaryLeft = new Bone().at(0, 1.5F, 1).feather(10, 14, 0, 0, -0.5F, 10, 14);
        final Bone feathersTertiaryRight = new Bone().at(0, 1.5F, 1).feather(0, 14, -10, 0, -0.5F, 10, 14);
        final Bone feathersSecondaryLeft = new Bone().at(0, 1, 0).feather(31, 14, -2, 0, -0.5F, 11, 12);
        final Bone feathersSecondaryRight = new Bone().at(0, 1, 0).feather(20, 14, -9, 0, -0.5F, 11, 12);
        final Bone feathersPrimaryLeft = new Bone().at(0, 0, 0).feather(53, 14, 0, -2.1F, -0.5F, 11, 11);
        final Bone feathersPrimaryRight = new Bone().at(0, 0, 0).feather(42, 14, -11, -2.1F, -0.5F, 11, 11);
        ulnaLeft.add(carpalsLeft).add(feathersSecondaryLeft);
        ulnaRight.add(carpalsRight).add(feathersSecondaryRight);
        carpalsLeft.add(feathersPrimaryLeft);
        carpalsRight.add(feathersPrimaryRight);
        humerusLeft.add(ulnaLeft).add(feathersTertiaryLeft);
        humerusRight.add(ulnaRight).add(feathersTertiaryRight);
        coracoidLeft.add(humerusLeft).add(feathersCoracoidLeft);
        coracoidRight.add(humerusRight).add(feathersCoracoidRight);
        this.root.add(coracoidLeft).add(coracoidRight);
        this.bonesLeft = List.of(coracoidLeft.part, humerusLeft.part, ulnaLeft.part, carpalsLeft.part);
        this.bonesRight = List.of(coracoidRight.part, humerusRight.part, ulnaRight.part, carpalsRight.part);
        this.feathersLeft = List.of(feathersCoracoidLeft.part, feathersTertiaryLeft.part, feathersSecondaryLeft.part, feathersPrimaryLeft.part);
        this.feathersRight = List.of(feathersCoracoidRight.part, feathersTertiaryRight.part, feathersSecondaryRight.part, feathersPrimaryRight.part);
    }

    @Override
    public void render(final AnimatorAvian animator, final float delta, final PoseStack pose, final VertexConsumer buf, final int light, final int overlay, final float flap, final float alpha) {
        for (int i = 0; i < this.bonesLeft.size(); i++) {
            setAngles(this.bonesLeft.get(i), this.bonesRight.get(i), animator.getWingRotation(i, delta).add(0.0D, flap * FLAP_WEIGHT[i], 0.0D));
        }
        for (int i = 0; i < this.feathersLeft.size(); i++) {
            setAngles(this.feathersLeft.get(i), this.feathersRight.get(i), animator.getFeatherRotation(i, delta));
        }
        this.root.part.render(pose, buf, light, overlay, FastColor.ARGB32.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F));
    }
}
