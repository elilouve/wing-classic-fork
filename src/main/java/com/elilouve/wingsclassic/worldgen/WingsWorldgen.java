package com.elilouve.wingsclassic.worldgen;

import com.elilouve.wingsclassic.WingsClassic;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class WingsWorldgen {
    private WingsWorldgen() {}

    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, WingsClassic.ID);

    public static final RegistryObject<PlacementModifierType<ConfigEnabledFilter>> CONFIG_ENABLED =
        PLACEMENT.register("config_enabled", () -> () -> ConfigEnabledFilter.CODEC);
}
