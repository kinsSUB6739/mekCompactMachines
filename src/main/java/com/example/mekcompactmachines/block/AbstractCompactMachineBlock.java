package com.example.mekcompactmachines.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCompactMachineBlock extends Block implements EntityBlock {
	public AbstractCompactMachineBlock(Properties properties) {
		super(properties);
	}
	@Override
	public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack heldItem = player.getItemInHand(hand);
		boolean isConfigurator = heldItem.getItem().getDescriptionId().contains("configurator");

		if (!level.isClientSide) {
			if (player.isCrouching() && isConfigurator) {
				level.destroyBlock(pos, true, player);
				return InteractionResult.SUCCESS;
			}
			if (!isConfigurator) {
				BlockEntity be = level.getBlockEntity(pos);
				if (be instanceof AbstractCompactMachineEntity) {
					NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) be, pos);
				}
			}
		}
		if (player.isCrouching() && isConfigurator) return InteractionResult.SUCCESS;
		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			if (!level.isClientSide) {
				BlockEntity blockEntity = level.getBlockEntity(pos);
				// もし、その場所にちゃんと「CompactCraftingTableBlockEntity（専用の箱）」が存在するなら
				// ★ 個別のクラス名ではなく、すべての親である「抽象クラス」で判定する！
				if (blockEntity instanceof AbstractCompactMachineEntity machineEntity) {
					// Entity側から IItemHandler を取得するメソッド（例: getItemHandler()）を呼び出す
					net.minecraftforge.items.IItemHandler itemHandler = machineEntity.getItemHandler();

					if (itemHandler != null) {
						for (int i = 0; i < itemHandler.getSlots(); i++) {
							ItemStack stack = itemHandler.getStackInSlot(i);
							if (!stack.isEmpty()) {
								Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
							}
						}
					}

					level.updateNeighbourForOutputSignal(pos, this);
				}
			}

			// 親クラスの本来の消滅処理を最後に必ず呼ぶ
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}
}
