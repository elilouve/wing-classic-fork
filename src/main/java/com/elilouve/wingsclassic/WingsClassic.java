package com.elilouve.wingsclassic;

import com.elilouve.wingsclassic.config.ClientConfig;
import com.elilouve.wingsclassic.config.WingsConfig;
import com.elilouve.wingsclassic.flight.Flights;
import com.elilouve.wingsclassic.net.WingsNetwork;
import com.elilouve.wingsclassic.registry.WingsBlocks;
import com.elilouve.wingsclassic.registry.WingsCreativeTabs;
import com.elilouve.wingsclassic.registry.WingsItems;
import com.elilouve.wingsclassic.registry.WingsSounds;
import com.elilouve.wingsclassic.worldgen.WingsWorldgen;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(WingsClassic.ID)
public final class WingsClassic {
    public static final String ID = "wingsclassic";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WingsClassic(final IEventBus modBus, final ModContainer container) {
        WingsBlocks.BLOCKS.register(modBus);
        WingsItems.ITEMS.register(modBus);
        WingsSounds.SOUNDS.register(modBus);
        WingsCreativeTabs.TABS.register(modBus);
        WingsWorldgen.PLACEMENT.register(modBus);
        Flights.ATTACHMENTS.register(modBus);
        modBus.addListener(WingsNetwork::register);
        container.registerConfig(ModConfig.Type.SERVER, WingsConfig.SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }
}
