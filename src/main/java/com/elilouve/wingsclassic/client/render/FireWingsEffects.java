package com.elilouve.wingsclassic.client.render;

import com.elilouve.wingsclassic.config.ClientConfig;
import com.elilouve.wingsclassic.flight.Flight;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/** Flammes et braises qui s'échappent des ailes de feu quand elles sont déployées. */
public final class FireWingsEffects {
    private FireWingsEffects() {}

    public static void spawn(final Player player, final Flight flight) {
        if (!ClientConfig.fireParticles()) {
            return;
        }
        final float amt = flight.getFlyingAmount(1.0F);
        if (amt < 0.3F) {
            return;
        }
        final Minecraft mc = Minecraft.getInstance();
        if (player == mc.player && mc.options.getCameraType().isFirstPerson()) {
            return;
        }
        final RandomSource rnd = player.getRandom();
        final float yaw = player.yBodyRot * Mth.DEG_TO_RAD;
        // Axes du corps : côté (gauche/droite) et « dos » qui passe de l'arrière à vers le haut en vol.
        final Vec3 side = new Vec3(Mth.cos(yaw), 0.0D, Mth.sin(yaw));
        final Vec3 back = new Vec3(Mth.sin(yaw), 0.0D, -Mth.cos(yaw)).scale(1.0F - amt).add(0.0D, amt, 0.0D);
        // En vol le corps pivote autour des pieds : les épaules passent de ~1,2 m de haut à ~0 m,
        // et le dos (où sont fixées les ailes) se trouve juste au-dessus.
        final Vec3 center = player.position().add(0.0D, 1.2D * (1.0F - amt), 0.0D).add(back.scale(0.3D));
        final Vec3 carried = player.getDeltaMovement().scale(0.85D);
        final int count = 2 + rnd.nextInt(2);
        for (int i = 0; i < count; i++) {
            final double lateral = (0.3D + rnd.nextDouble() * 0.8D) * (rnd.nextBoolean() ? 1.0D : -1.0D);
            final Vec3 pos = center.add(side.scale(lateral)).add(back.scale(rnd.nextDouble() * 0.15D))
                .add(0.0D, (rnd.nextDouble() - 0.5D) * 0.2D, 0.0D);
            final Vec3 vel = carried
                .add((rnd.nextDouble() - 0.5D) * 0.02D, 0.01D + rnd.nextDouble() * 0.02D, (rnd.nextDouble() - 0.5D) * 0.02D);
            final int roll = rnd.nextInt(10);
            player.level().addParticle(roll < 6 ? ParticleTypes.FLAME : roll < 9 ? ParticleTypes.SMALL_FLAME : ParticleTypes.LAVA, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
        }
        if (rnd.nextInt(4) == 0) {
            final Vec3 pos = center.add(side.scale((rnd.nextDouble() - 0.5D) * 1.6D));
            player.level().addParticle(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, carried.x, carried.y + 0.02D, carried.z);
        }
    }
}
