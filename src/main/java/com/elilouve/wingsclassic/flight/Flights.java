package com.elilouve.wingsclassic.flight;

import com.elilouve.wingsclassic.WingsClassic;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public final class Flights {
    private Flights() {}

    public static final ResourceLocation KEY = new ResourceLocation(WingsClassic.ID, "flight");

    public static final Capability<Flight> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    @Nullable
    public static Flight get(final Entity entity) {
        if (entity instanceof Player) {
            return entity.getCapability(CAPABILITY).resolve().orElse(null);
        }
        return null;
    }

    public static void ifPlayer(final Entity entity, final BiConsumer<Player, Flight> action) {
        final Flight flight = get(entity);
        if (flight != null) {
            action.accept((Player) entity, flight);
        }
    }

    public static final class Provider implements ICapabilitySerializable<CompoundTag> {
        private final FlightDefault flight = new FlightDefault();
        private final LazyOptional<Flight> optional = LazyOptional.of(() -> this.flight);

        public FlightDefault flight() {
            return this.flight;
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull final Capability<T> cap, @Nullable final Direction side) {
            return CAPABILITY.orEmpty(cap, this.optional);
        }

        public void invalidate() {
            this.optional.invalidate();
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.flight.save();
        }

        @Override
        public void deserializeNBT(final CompoundTag tag) {
            this.flight.load(tag);
        }
    }
}
