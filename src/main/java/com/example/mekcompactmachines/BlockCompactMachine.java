package com.example.mekcompactmachines;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
public class BlockCompactMachine extends Block implements EntityBlock {
	public BlockCompactMachine(Properties properties) {
		super(properties);
	}
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CompactMachineBlockEntity(pos, state);
	}
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack heldItem = player.getItemInHand(hand);
		boolean isConfigurator = heldItem.getItem().getDescriptionId().contains("configurator");

		if (!level.isClientSide) {
			if (player.isCrouching() && isConfigurator) {
				level.destroyBlock(pos, true, player);
				return InteractionResult.SUCCESS;
			}
			if (!isConfigurator) {
				BlockEntity be = level.getBlockEntity(pos);
				if (be instanceof CompactMachineBlockEntity) {
					NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) be, pos);
				}
			}
		}
		if (player.isCrouching() && isConfigurator) return InteractionResult.SUCCESS;
		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof CompactMachineBlockEntity) {
				((CompactMachineBlockEntity) blockEntity).drops();
			}
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (level.isClientSide()) {
			return null;
		}
		return createTickerHelper(type, ModBlockEntities.COMPACT_MACHINE_BE.get(),
				CompactMachineBlockEntity::tick);
	}
	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
			BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
		return pClientType == pServerType ? (BlockEntityTicker<A>) pTicker : null;
	}
}