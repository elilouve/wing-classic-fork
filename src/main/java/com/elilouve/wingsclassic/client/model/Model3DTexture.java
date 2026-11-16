package com.elilouve.wingsclassic.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Direction;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.EnumSet;

/**
 * Plume « 3D » : une texture 2D extrudée sur 1 pixel d'épaisseur (comme dans le mod d'origine).
 * Cube sans faces vanilla, qui dessine ses propres quads.
 */
public final class Model3DTexture extends ModelPart.Cube {
    private final float x0, y0, z0, x1, y1, z1;
    private final int width;
    private final int height;
    private final float u1, v1, u2, v2;

    private Model3DTexture(
        final float posX, final float posY, final float posZ,
        final int width, final int height,
        final float u1, final float v1, final float u2, final float v2
    ) {
        super(0, 0, posX, posY, posZ, 0, 0, 0, 0, 0, 0, false, 64, 64, EnumSet.noneOf(Direction.class));
        this.x0 = posX;
        this.y0 = posY;
        this.z0 = posZ;
        this.x1 = posX + width;
        this.y1 = posY + height;
        this.z1 = posZ + 1;
        this.width = width;
        this.height = height;
        this.u1 = u1;
        this.v1 = v1;
        this.u2 = u2;
        this.v2 = v2;
    }

    public static Model3DTexture create(
        final float posX, final float posY, final float posZ,
        final int width, final int height,
        final int u, final int v,
        final int textureWidth, final int textureHeight
    ) {
        return new Model3DTexture(
            posX, posY, posZ, width, height,
            (float) u / textureWidth, (float) v / textureHeight,
            (float) (u + width) / textureWidth, (float) (v + height) / textureHeight
        );
    }

    @Override
    public void compile(final PoseStack.Pose pose, final VertexConsumer buf, final int light, final int overlay, final int color) {
        final Matrix4f m = pose.pose();
        final Matrix3f n = pose.normal();
        // Faces avant et arrière
        this.quad(m, n, buf, light, overlay, color, 0, 0, -1,
            this.x1, this.y0, this.z0, this.u2, this.v1,
            this.x0, this.y0, this.z0, this.u1, this.v1,
            this.x0, this.y1, this.z0, this.u1, this.v2,
            this.x1, this.y1, this.z0, this.u2, this.v2);
        this.quad(m, n, buf, light, overlay, color, 0, 0, 1,
            this.x1, this.y1, this.z1, this.u2, this.v2,
            this.x0, this.y1, this.z1, this.u1, this.v2,
            this.x0, this.y0, this.z1, this.u1, this.v1,
            this.x1, this.y0, this.z1, this.u2, this.v1);
        final float du = 0.5F * (this.u1 - this.u2) / this.width;
        final float dv = 0.5F * (this.v1 - this.v2) / this.height;
        // Tranches verticales (gauche puis droite)
        for (int k = 0; k < this.width; k++) {
            final float x = this.x0 + k;
            final float u = this.u1 + (this.u2 - this.u1) * ((float) k / this.width) - du;
            this.quad(m, n, buf, light, overlay, color, -1, 0, 0,
                x, this.y0, this.z0, u, this.v1,
                x, this.y0, this.z1, u, this.v1,
                x, this.y1, this.z1, u, this.v2,
                x, this.y1, this.z0, u, this.v2);
        }
        for (int k = 0; k < this.width; k++) {
            final float u = this.u1 + (this.u2 - this.u1) * ((float) k / this.width) - du;
            final float x = this.x0 + k + 1;
            this.quad(m, n, buf, light, overlay, color, 1, 0, 0,
                x, this.y1, this.z0, u, this.v2,
                x, this.y1, this.z1, u, this.v2,
                x, this.y0, this.z1, u, this.v1,
                x, this.y0, this.z0, u, this.v1);
        }
        // Tranches horizontales (haut puis bas)
        for (int k = 0; k < this.height; k++) {
            final float v = this.v1 + (this.v2 - this.v1) * ((float) k / this.height) - dv;
            final float y = this.y0 + k + 1;
            this.quad(m, n, buf, light, overlay, color, 0, 1, 0,
                this.x1, y, this.z0, this.u2, v,
                this.x0, y, this.z0, this.u1, v,
                this.x0, y, this.z1, this.u1, v,
                this.x1, y, this.z1, this.u2, v);
        }
        for (int k = 0; k < this.height; k++) {
            final float v = this.v1 + (this.v2 - this.v1) * ((float) k / this.height) - dv;
            final float y = this.y0 + k;
            this.quad(m, n, buf, light, overlay, color, 0, -1, 0,
                this.x0, y, this.z0, this.u1, v,
                this.x1, y, this.z0, this.u2, v,
                this.x1, y, this.z1, this.u2, v,
                this.x0, y, this.z1, this.u1, v);
        }
    }

    private void quad(
        final Matrix4f m, final Matrix3f n, final VertexConsumer buf,
        final int light, final int overlay, final int color,
        final float nx, final float ny, final float nz,
        final float ax, final float ay, final float az, final float au, final float av,
        final float bx, final float by, final float bz, final float bu, final float bv,
        final float cx, final float cy, final float cz, final float cu, final float cv,
        final float dx, final float dy, final float dz, final float du, final float dv
    ) {
        final Vector3f normal = n.transform(new Vector3f(nx, ny, nz));
        this.vertex(m, buf, light, overlay, color, normal, ax, ay, az, au, av);
        this.vertex(m, buf, light, overlay, color, normal, bx, by, bz, bu, bv);
        this.vertex(m, buf, light, overlay, color, normal, cx, cy, cz, cu, cv);
        this.vertex(m, buf, light, overlay, color, normal, dx, dy, dz, du, dv);
    }

    private void vertex(
        final Matrix4f m, final VertexConsumer buf, final int light, final int overlay,
        final int color, final Vector3f normal,
        final float x, final float y, final float z, final float u, final float v
    ) {
        final Vector4f p = m.transform(new Vector4f(x / 16.0F, y / 16.0F, z / 16.0F, 1.0F));
        buf.addVertex(p.x(), p.y(), p.z()).setColor(color).setUv(u, v).setOverlay(overlay).setLight(light).setNormal(normal.x(), normal.y(), normal.z());
    }
}
