package com.elilouve.wingsclassic.registry;

import com.elilouve.wingsclassic.WingsClassic;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class WingsSounds {
    private WingsSounds() {}

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, WingsClassic.ID);

    public static final RegistryObject<SoundEvent> EQUIP_WINGS = register("item.armor.equip_wings");
    public static final RegistryObject<SoundEvent> WINGS_FLYING = register("item.wings.flying");

    private static RegistryObject<SoundEvent> register(final String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WingsClassic.ID, name)));
    }
}
