package com.elilouve.wingsclassic.registry;

import com.elilouve.wingsclassic.WingsClassic;
import com.elilouve.wingsclassic.item.WingsItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class WingsItems {
    private WingsItems() {}

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WingsClassic.ID);

    private static final int EASIEST = 960;
    private static final int MODERATE = 1920;
    private static final int HARDEST = 2880;

    public static final RegistryObject<Item> FAIRY_DUST_ORE = ITEMS.register(
        "fairy_dust_ore",
        () -> new BlockItem(WingsBlocks.FAIRY_DUST_ORE.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> AMETHYST_ORE = ITEMS.register(
        "amethyst_ore",
        () -> new BlockItem(WingsBlocks.AMETHYST_ORE.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> DEEPSLATE_FAIRY_DUST_ORE = ITEMS.register(
        "deepslate_fairy_dust_ore",
        () -> new BlockItem(WingsBlocks.DEEPSLATE_FAIRY_DUST_ORE.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> DEEPSLATE_AMETHYST_ORE = ITEMS.register(
        "deepslate_amethyst_ore",
        () -> new BlockItem(WingsBlocks.DEEPSLATE_AMETHYST_ORE.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> FAIRY_DUST = ITEMS.register("fairy_dust", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> AMETHYST = ITEMS.register("amethyst", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BAT_BLOOD = ITEMS.register(
        "bat_blood",
        () -> new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE))
    );

    public static final RegistryObject<Item> ANGEL_WINGS = wings("angel_wings", EASIEST);

    public static final RegistryObject<Item> SLIME_WINGS = wings("slime_wings", EASIEST);

    public static final RegistryObject<Item> BLUE_BUTTERFLY_WINGS = wings("blue_butterfly_wings", EASIEST);

    public static final RegistryObject<Item> MONARCH_BUTTERFLY_WINGS = wings("monarch_butterfly_wings", EASIEST);

    public static final RegistryObject<Item> FIRE_WINGS = wings("fire_wings", MODERATE);

    public static final RegistryObject<Item> BAT_WINGS = wings("bat_wings", MODERATE);

    public static final RegistryObject<Item> FAIRY_WINGS = wings("fairy_wings", EASIEST);

    public static final RegistryObject<Item> EVIL_WINGS = wings("evil_wings", MODERATE);

    public static final RegistryObject<Item> DRAGON_WINGS = wings("dragon_wings", HARDEST);

    private static RegistryObject<Item> wings(final String name, final int durability) {
        return ITEMS.register(name, () -> new WingsItem(name, durability));
    }
}
