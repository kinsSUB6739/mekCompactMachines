package com.example.mekcompactmachines.block.CompactCraftor;

import com.example.mekcompactmachines.block.AbstractCompactMachineBlock;
import com.example.mekcompactmachines.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CompactCraftorBlock extends AbstractCompactMachineBlock {
	public CompactCraftorBlock(Properties properties) {
		super(properties);
	}

	// ① 作業台専用の BlockEntity を生み出す
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CompactCraftorEntity(pos, state);
	}

	// ② 毎Tickの自動クラフト処理（tick）を動かすための設定
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (level.isClientSide()) {
			return null;
		}
		return createTickerHelper(type, ModBlockEntities.COMPACT_CRAFTOR.get(),CompactCraftorEntity::tick);
	}

	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
			BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
		return pClientType == pServerType ? (BlockEntityTicker<A>) pTicker : null;
	}
}
