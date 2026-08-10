package com.example.mekcompactmachines.init;
import com.example.mekcompactmachines.MyMekAddon;
import com.example.mekcompactmachines.block.CompactCraftor.CompactCraftorBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MyMekAddon.MODID);
    public static final RegistryObject<Block> COMPACT_CRAFTOR = BLOCKS.register("compact_craftor",
            () -> new CompactCraftorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0f).requiresCorrectToolForDrops()));
//    public static final RegistryObject<Block> COMPACT_INDUCTION_MATRIX = BLOCKS.register("compact_induction_matrix",
//            () -> new BlockCompactMachine(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0f).requiresCorrectToolForDrops()));
}