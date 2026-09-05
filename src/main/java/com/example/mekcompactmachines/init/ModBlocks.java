package com.example.mekcompactmachines.init;

import com.example.mekcompactmachines.ModConstants;
import com.example.mekcompactmachines.MyMekAddon;
import com.example.mekcompactmachines.block.CompactCraftor.CompactCrafterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Modで使用するすべてのブロックを管理・登録するクラス。
 * <p>
 * {@link DeferredRegister} を利用して Forge のブロックレジストリに登録を行います。
 */
public class ModBlocks {

    /** ブロックのレジストリインスタンス */
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModConstants.MOD_ID);

    /**
     * Compact Crafter ブロック
     * <p>
     * 特性: 金属のマップカラー、硬さ 5.0、適切なツールでのみドロップする
     */
    public static final RegistryObject<Block> COMPACT_CRAFTER = BLOCKS.register(ModConstants.COMPACT_CRAFTER,
            () -> new CompactCrafterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(5.0f)
                    .requiresCorrectToolForDrops()));
}