package com.example.mekcompactmachines;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData; // 追加
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CompactMachineBlockEntity extends BlockEntity implements MenuProvider {
	private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}

		@Override
		public int getSlotLimit(int slot) {
			// スロット容量拡張
			if (slot == 0 || slot == 1) {
				return 2048;
			}
			return 64;
		}
	};

	private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

	// ■ 追加: 進行度管理用の変数
	protected final ContainerData data;
	private int progress = 0;
	private int maxProgress = 200; // 10秒 (20tick * 10s = 200)

	public CompactMachineBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.COMPACT_MACHINE_BE.get(), pos, state);

		// ■ 追加: GUIと同期するためのデータ定義
		this.data = new ContainerData() {
			@Override
			public int get(int index) {
				return switch (index) {
					case 0 -> CompactMachineBlockEntity.this.progress;
					case 1 -> CompactMachineBlockEntity.this.maxProgress;
					default -> 0;
				};
			}

			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0 -> CompactMachineBlockEntity.this.progress = value;
					case 1 -> CompactMachineBlockEntity.this.maxProgress = value;
				}
			}

			@Override
			public int getCount() {
				return 2; // データの数 (progress, maxProgress)
			}
		};
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return lazyItemHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		lazyItemHandler = LazyOptional.of(() -> itemHandler);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		lazyItemHandler.invalidate();
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		nbt.put("inventory", itemHandler.serializeNBT());
		// ■ 追加: 進行度をセーブデータに保存
		nbt.putInt("compact_machine.progress", progress);
		super.saveAdditional(nbt);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		itemHandler.deserializeNBT(nbt.getCompound("inventory"));
		progress = nbt.getInt("compact_machine.progress");
	}

	public void drops() {
		SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
		for (int i = 0; i < itemHandler.getSlots(); i++) {
			inventory.setItem(i, itemHandler.getStackInSlot(i));
		}
		Containers.dropContents(this.level, this.worldPosition, inventory);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("block.mek_compact_machines.compact_crafting_table");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
		return new CompactMachineMenu(containerId, playerInventory, this, this.data);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, CompactMachineBlockEntity pEntity) {
		if (level.isClientSide()) {
			return;
		}
		ItemStack slot0 = pEntity.itemHandler.getStackInSlot(0);
		ItemStack slot1 = pEntity.itemHandler.getStackInSlot(1);
		ItemStack slot2 = pEntity.itemHandler.getStackInSlot(2);
		ItemStack slot3 = pEntity.itemHandler.getStackInSlot(3);
		boolean hasGlass = slot0.getCount() >= 1536;
		boolean hasCasing = slot1.getCount() >= 200;
		boolean hasCardboard = slot2.getCount() >= 1;
		boolean canOutput = slot3.isEmpty() ||
				(slot3.getItem() == ModItems.COMPACT_INDUCTION_MATRIX.get() && slot3.getCount() < slot3.getMaxStackSize());
		if (hasGlass && hasCasing && hasCardboard && canOutput) {
			pEntity.progress++;
			setChanged(level, pos, state);
			if (pEntity.progress >= pEntity.maxProgress) {
				craftItem(pEntity);
			}
		} else {
			pEntity.resetProgress();
			setChanged(level, pos, state);
		}
	}

	private static void craftItem(CompactMachineBlockEntity pEntity) {
		ItemStack glassStack = pEntity.itemHandler.getStackInSlot(0);
		glassStack.shrink(1536);
		pEntity.itemHandler.setStackInSlot(0, glassStack);
		ItemStack casingStack = pEntity.itemHandler.getStackInSlot(1);
		casingStack.shrink(200);
		pEntity.itemHandler.setStackInSlot(1, casingStack);
		ItemStack cardboardStack = pEntity.itemHandler.getStackInSlot(2);
		cardboardStack.shrink(1);
		pEntity.itemHandler.setStackInSlot(2, cardboardStack);
		pEntity.itemHandler.insertItem(3, new ItemStack(ModBlocks.COMPACT_INDUCTION_MATRIX.get(), 1), false);
		pEntity.resetProgress();
	}
	private void resetProgress() {
		this.progress = 0;
	}
}