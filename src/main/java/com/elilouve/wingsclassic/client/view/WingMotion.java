package com.elilouve.wingsclassic.client.view;

import com.elilouve.wingsclassic.util.WingsMath;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

/**
 * « Physique » secondaire des ailes : elles balancent à la marche, suivent la course
 * et réagissent aux sauts et aux chutes (ressort amorti), en plus de l'animation principale.
 */
public final class WingMotion {
    private float lag, prevLag, lagVel;
    private float run, prevRun;
    private float phase, prevPhase;

    public void tick(final Player p) {
        final double dx = p.getX() - p.xo;
        final double dz = p.getZ() - p.zo;
        final double dy = p.getY() - p.yo;
        final float speed = (float) Math.sqrt(dx * dx + dz * dz);
        this.prevRun = this.run;
        this.prevPhase = this.phase;
        this.prevLag = this.lag;
        final float targetRun = p.onGround() ? Mth.clamp(speed / 0.28F, 0.0F, 1.0F) : this.run * 0.9F;
        this.run += (targetRun - this.run) * 0.3F;
        if (p.onGround()) {
            this.phase += speed * 2.6F;
        }
        // Ressort : le saut/la chute pousse les ailes, puis elles reviennent en oscillant.
        final float push = Mth.clamp((float) dy * 4.0F, -1.0F, 1.0F);
        this.lagVel += (push - this.lag) * 0.25F;
        this.lagVel *= 0.62F;
        this.lag += this.lagVel;
        this.lag = Mth.clamp(this.lag, -1.5F, 1.5F);
    }

    /** Décalage (en degrés) ajouté au battement des ailes. */
    public float flap(final float delta, final float strength) {
        final float lagNow = WingsMath.lerp(this.prevLag, this.lag, delta);
        final float runNow = WingsMath.lerp(this.prevRun, this.run, delta);
        final float phaseNow = WingsMath.lerp(this.prevPhase, this.phase, delta);
        final float sway = Mth.sin(phaseNow) * runNow * 6.0F + runNow * 4.0F;
        return (lagNow * 22.0F + sway) * strength;
    }
}
