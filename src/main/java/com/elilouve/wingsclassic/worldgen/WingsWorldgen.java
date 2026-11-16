package com.elilouve.wingsclassic.worldgen;

import com.elilouve.wingsclassic.WingsClassic;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class WingsWorldgen {
    private WingsWorldgen() {}

    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, WingsClassic.ID);

    public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<ConfigEnabledFilter>> CONFIG_ENABLED =
        PLACEMENT.register("config_enabled", WingsWorldgen::configEnabledType);

    private static PlacementModifierType<ConfigEnabledFilter> configEnabledType() {
        return () -> ConfigEnabledFilter.CODEC;
    }
}
