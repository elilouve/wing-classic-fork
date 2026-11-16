package com.elilouve.wingsclassic.registry;

import com.elilouve.wingsclassic.WingsClassic;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class WingsBlocks {
    private WingsBlocks() {}

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WingsClassic.ID);

    public static final RegistryObject<Block> FAIRY_DUST_ORE = BLOCKS.register(
        "fairy_dust_ore",
        () -> new DropExperienceBlock(oreProperties(), UniformInt.of(0, 2))
    );

    public static final RegistryObject<Block> AMETHYST_ORE = BLOCKS.register(
        "amethyst_ore",
        () -> new DropExperienceBlock(oreProperties(), UniformInt.of(3, 7))
    );

    public static final RegistryObject<Block> DEEPSLATE_FAIRY_DUST_ORE = BLOCKS.register(
        "deepslate_fairy_dust_ore",
        () -> new DropExperienceBlock(deepslateProperties(), UniformInt.of(0, 2))
    );

    public static final RegistryObject<Block> DEEPSLATE_AMETHYST_ORE = BLOCKS.register(
        "deepslate_amethyst_ore",
        () -> new DropExperienceBlock(deepslateProperties(), UniformInt.of(3, 7))
    );

    private static BlockBehaviour.Properties oreProperties() {
        return BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops()
            .strength(3.0F, 5.0F);
    }

    private static BlockBehaviour.Properties deepslateProperties() {
        return BlockBehaviour.Properties.of()
            .mapColor(MapColor.DEEPSLATE)
            .requiresCorrectToolForDrops()
            .strength(4.5F, 3.0F)
            .sound(SoundType.DEEPSLATE);
    }
}
