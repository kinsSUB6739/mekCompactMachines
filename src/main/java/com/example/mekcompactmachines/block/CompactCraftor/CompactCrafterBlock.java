package com.example.mekcompactmachines.block.CompactCraftor;

import com.example.mekcompactmachines.block.AbstractCompactMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Compact Crafter（コンパクトクラフター）のブロッククラス。
 * <p>
 * {@link AbstractCompactMachineBlock} を継承し、専用のブロックエンティティ（{@link CompactCrafterEntity}）の生成を行います。
 */
public class CompactCrafterBlock extends AbstractCompactMachineBlock {

    /**
     * コンストラクタ。
     *
     * @param properties ブロックの材質や硬さなどの基本プロパティ
     */
    public CompactCrafterBlock(Properties properties) {
        super(properties);
    }

    /**
     * このブロックに対応する専用のブロックエンティティ（BlockEntity）を新規作成します。
     *
     * @param pos   ブロックの設置座標
     * @param state ブロックの現在の状態
     * @return CompactCrafterEntity のインスタンス
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CompactCrafterEntity(pos, state);
    }
}