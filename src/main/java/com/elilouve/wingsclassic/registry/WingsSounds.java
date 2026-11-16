package com.elilouve.wingsclassic.registry;

import com.elilouve.wingsclassic.WingsClassic;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class WingsSounds {
    private WingsSounds() {}

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, WingsClassic.ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> EQUIP_WINGS = register("item.armor.equip_wings");
    public static final DeferredHolder<SoundEvent, SoundEvent> WINGS_FLYING = register("item.wings.flying");

    private static DeferredHolder<SoundEvent, SoundEvent> register(final String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(WingsClassic.ID, name)));
    }
}
