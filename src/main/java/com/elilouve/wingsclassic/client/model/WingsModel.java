package com.elilouve.wingsclassic.client.model;

import com.elilouve.wingsclassic.client.anim.Animator;
import com.elilouve.wingsclassic.util.WingsMath;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Base des modèles d'ailes, construits à la main avec des ModelPart. */
public abstract class WingsModel<A extends Animator> {
    protected static final int TEX_SIZE = 64;

    /** flap = décalage de battement en degrés (physique), alpha = opacité (1 = normal). */
    public abstract void render(A animator, float delta, PoseStack pose, VertexConsumer buf, int light, int overlay, float flap, float alpha);

    /** Un os : un ModelPart que l'on peut remplir d'enfants après sa création. */
    protected static final class Bone {
        final List<ModelPart.Cube> cubes = new ArrayList<>();
        final Map<String, ModelPart> children = new HashMap<>();
        final ModelPart part = new ModelPart(this.cubes, this.children);
        private int counter;

        Bone at(final float x, final float y, final float z) {
            this.part.setPos(x, y, z);
            return this;
        }

        Bone box(final int u, final int v, final float x, final float y, final float z, final int w, final int h, final int d) {
            this.cubes.add(CubeListBuilder.create().texOffs(u, v).addBox(x, y, z, w, h, d, CubeDeformation.NONE).getCubes().get(0).bake(TEX_SIZE, TEX_SIZE));
            return this;
        }

        Bone feather(final int u, final int v, final float x, final float y, final float z, final int w, final int h) {
            this.cubes.add(Model3DTexture.create(x, y, z, w, h, u, v, TEX_SIZE, TEX_SIZE));
            return this;
        }

        Bone add(final Bone child) {
            this.children.put("c" + this.counter++, child.part);
            return this;
        }
    }

    protected static void setAngles(final ModelPart left, final ModelPart right, final Vec3 angles) {
        right.xRot = left.xRot = WingsMath.toRadians((float) angles.x);
        right.yRot = -(left.yRot = WingsMath.toRadians((float) angles.y));
        right.zRot = -(left.zRot = WingsMath.toRadians((float) angles.z));
    }
}
