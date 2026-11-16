package com.elilouve.wingsclassic.registry;

import com.elilouve.wingsclassic.WingsClassic;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class WingsCreativeTabs {
    private WingsCreativeTabs() {}

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WingsClassic.ID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register(
        "main",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + WingsClassic.ID))
            .icon(() -> new ItemStack(WingsItems.ANGEL_WINGS.get()))
            .displayItems((parameters, output) -> WingsItems.ITEMS.getEntries().forEach(item -> output.accept(item.get())))
            .build()
    );
}
