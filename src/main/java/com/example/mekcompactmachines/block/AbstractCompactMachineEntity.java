package com.example.mekcompactmachines.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCompactMachineEntity extends BlockEntity implements MenuProvider {

	protected final ItemStackHandler itemHandler;
	// ★追加: Forgeのシステムにインベントリを教えるための「包み紙」
	private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

	public AbstractCompactMachineEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int slotCount) {
		super(type, pos, state);
		this.itemHandler = new ItemStackHandler(slotCount) {
			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
			}
		};
	}

	public IItemHandler getItemHandler() {
		return this.itemHandler;
	}

	// ★追加: Capabilityを取得されたときに、自分のitemHandlerを返す
	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return lazyItemHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	// ★追加: ブロックが置かれたとき（読み込まれたとき）に包み紙の中身を入れる
	@Override
	public void onLoad() {
		super.onLoad();
		lazyItemHandler = LazyOptional.of(() -> itemHandler);
	}

	// ★追加: ブロックが壊されたときに包み紙を破棄する（メモリリーク防止）
	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		lazyItemHandler.invalidate();
	}
}