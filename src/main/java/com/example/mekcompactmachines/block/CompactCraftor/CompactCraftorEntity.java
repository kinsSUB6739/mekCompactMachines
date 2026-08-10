package com.example.mekcompactmachines.block.CompactCraftor;

import com.example.mekcompactmachines.block.AbstractCompactMachineEntity;
import com.example.mekcompactmachines.init.ModBlockEntities;
import com.example.mekcompactmachines.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class CompactCraftorEntity extends AbstractCompactMachineEntity {
	private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}

		@Override
		public int getSlotLimit(int slot) {
			if (slot == 0 || slot == 1) {
				return 2048;
			}
			return 64;
		}
	};

	protected final ContainerData data;
	private int progress = 0;
	private int maxProgress = 200;

	public CompactCraftorEntity(BlockPos pos, BlockState state) {
		// ★修正1: スロット数を実際の数に合わせて「4」にする！
		super(ModBlockEntities.COMPACT_CRAFTOR.get(), pos, state, 4);

		this.data = new ContainerData() {
			@Override
			public int get(int index) {
				return switch (index) {
					case 0 -> CompactCraftorEntity.this.progress;
					case 1 -> CompactCraftorEntity.this.maxProgress;
					default -> 0;
				};
			}

			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0 -> CompactCraftorEntity.this.progress = value;
					case 1 -> CompactCraftorEntity.this.maxProgress = value;
				}
			}

			@Override
			public int getCount() {
				return 2;
			}
		};
	}

	// ★修正2: 親クラスが要求するインベントリの窓口（getter）を実装する
	@Override
	public IItemHandler getItemHandler() {
		return this.itemHandler;
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		nbt.put("inventory", itemHandler.serializeNBT());
		nbt.putInt("compact_machine.progress", progress);
		super.saveAdditional(nbt);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		itemHandler.deserializeNBT(nbt.getCompound("inventory"));
		progress = nbt.getInt("compact_machine.progress");
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("block.mek_compact_machines.compact_crafting_table");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
		return new CompactCraftorMenu(containerId, playerInventory, this, this.data);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, CompactCraftorEntity pEntity) {
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
		boolean canOutput = slot3.isEmpty();

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

	private static void craftItem(CompactCraftorEntity pEntity) {
		pEntity.itemHandler.getStackInSlot(0).shrink(1536);
		pEntity.itemHandler.getStackInSlot(1).shrink(200);
		pEntity.itemHandler.getStackInSlot(2).shrink(1);
		pEntity.itemHandler.insertItem(3, new ItemStack(ModItems.SUBSPACE_CARDBOARD.get(), 1), false);
		pEntity.resetProgress();
	}

	private void resetProgress() {
		this.progress = 0;
	}
}