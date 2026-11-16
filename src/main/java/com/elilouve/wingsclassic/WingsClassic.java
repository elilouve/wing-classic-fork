package com.elilouve.wingsclassic;

import com.elilouve.wingsclassic.config.ClientConfig;
import com.elilouve.wingsclassic.config.WingsConfig;
import com.elilouve.wingsclassic.flight.Flight;
import com.elilouve.wingsclassic.net.WingsNetwork;
import com.elilouve.wingsclassic.registry.WingsBlocks;
import com.elilouve.wingsclassic.registry.WingsCreativeTabs;
import com.elilouve.wingsclassic.registry.WingsItems;
import com.elilouve.wingsclassic.registry.WingsSounds;
import com.elilouve.wingsclassic.worldgen.WingsWorldgen;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(WingsClassic.ID)
public final class WingsClassic {
    public static final String ID = "wingsclassic";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WingsClassic() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        WingsBlocks.BLOCKS.register(modBus);
        WingsItems.ITEMS.register(modBus);
        WingsSounds.SOUNDS.register(modBus);
        WingsCreativeTabs.TABS.register(modBus);
        WingsWorldgen.PLACEMENT.register(modBus);
        modBus.addListener(this::onRegisterCapabilities);
        modBus.addListener(this::onCommonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, WingsConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }

    private void onRegisterCapabilities(final RegisterCapabilitiesEvent event) {
        event.register(Flight.class);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(WingsNetwork::register);
    }
}
