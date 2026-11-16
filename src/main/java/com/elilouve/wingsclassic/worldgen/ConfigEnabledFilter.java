package com.elilouve.wingsclassic.worldgen;

import com.elilouve.wingsclassic.config.WingsConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

/** Filtre de génération : laisse passer le minerai seulement si la config l'autorise. */
public final class ConfigEnabledFilter extends PlacementFilter {
    public static final Codec<ConfigEnabledFilter> CODEC = RecordCodecBuilder.create(i ->
        i.group(Codec.STRING.fieldOf("ore").forGetter(f -> f.ore)).apply(i, ConfigEnabledFilter::new));

    private final String ore;

    public ConfigEnabledFilter(final String ore) {
        this.ore = ore;
    }

    @Override
    protected boolean shouldPlace(final PlacementContext context, final RandomSource random, final BlockPos pos) {
        return "amethyst".equals(this.ore) ? WingsConfig.amethystOre() : WingsConfig.fairyDustOre();
    }

    @Override
    public PlacementModifierType<?> type() {
        return WingsWorldgen.CONFIG_ENABLED.get();
    }
}
