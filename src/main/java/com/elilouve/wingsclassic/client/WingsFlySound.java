package com.elilouve.wingsclassic.client;

import com.elilouve.wingsclassic.config.ClientConfig;
import com.elilouve.wingsclassic.flight.Flight;
import com.elilouve.wingsclassic.registry.WingsSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

/** Bruit de vent des ailes, dont le volume suit la vitesse (repris du mod d'origine). */
public final class WingsFlySound extends AbstractTickableSoundInstance {
    private final Player player;
    private final Flight flight;

    public WingsFlySound(final Player player, final Flight flight) {
        super(WingsSounds.WINGS_FLYING.get(), SoundSource.PLAYERS, player.getRandom());
        this.player = player;
        this.flight = flight;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.01F;
    }

    /** Le son démarre à volume quasi nul (le vent n'est audible qu'en vol) : sans ça, Minecraft ne le lance jamais. */
    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        if (this.player.isRemoved()) {
            this.stop();
        } else if (this.flight.getFlyingAmount(1.0F) > 0.0F) {
            this.x = this.player.getX();
            this.y = this.player.getY();
            this.z = this.player.getZ();
            final float velocity = (float) this.player.getDeltaMovement().length();
            if (velocity >= 0.01F) {
                // Plus audible que l'élytre : 0.7 bloc/tick (vol normal) -> ~55 %, 1.5 (sprint) -> 100 %
                this.volume = Mth.clamp(velocity * 0.8F, 0.0F, 1.0F) * ClientConfig.soundVolume();
            } else {
                this.volume = 0.0F;
            }
            final float cutoff = 0.8F;
            this.pitch = this.volume > cutoff ? 1.0F + (this.volume - cutoff) : 1.0F;
        } else {
            this.volume = 0.0F;
        }
    }
}
