package com.example.mekcompactmachines;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MyMekAddon.MODID);
    public static final RegistryObject<Block> COMPACT_CRAFTING_TABLE = BLOCKS.register("compact_crafting_table",
            () -> new BlockCompactMachine(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> COMPACT_INDUCTION_MATRIX = BLOCKS.register("compact_induction_matrix",
            () -> new BlockCompactMachine(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0f).requiresCorrectToolForDrops()));
}