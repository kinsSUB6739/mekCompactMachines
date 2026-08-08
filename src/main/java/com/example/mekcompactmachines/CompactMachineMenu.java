package com.example.mekcompactmachines;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
public class CompactMachineMenu extends AbstractContainerMenu {
	public final CompactMachineBlockEntity blockEntity;
	private final ContainerLevelAccess levelAccess;
	// ■ 追加: データを保持する変数
	private final ContainerData data;
	public CompactMachineMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
		this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
	}
	public CompactMachineMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {

		super(ModMenuTypes.COMPACT_MACHINE_MENU.get(), containerId);
		blockEntity = (CompactMachineBlockEntity) entity;
		this.levelAccess = ContainerLevelAccess.create(inv.player.level(), entity.getBlockPos());
		this.data = data;
		addDataSlots(data);
		addPlayerInventory(inv);
		addPlayerHotbar(inv);
		blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
			// ■ スロット0: 構造用ガラス (mekanism:structural_glass) 専用
			this.addSlot(new SlotItemHandler(handler, 0, 27, 26) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					ResourceLocation regName = ForgeRegistries.ITEMS.getKey(stack.getItem());
					return regName != null && regName.toString().equals("mekanism:structural_glass");
				}
				@Override
				public int getMaxStackSize(@NotNull ItemStack stack) {
					return this.getItemHandler().getSlotLimit(this.getSlotIndex());
				}
			});
			this.addSlot(new SlotItemHandler(handler, 1, 27, 45) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					ResourceLocation regName = ForgeRegistries.ITEMS.getKey(stack.getItem());
					return regName != null && regName.toString().equals("mekanism:induction_casing");
				}
				@Override
				public int getMaxStackSize(@NotNull ItemStack stack) {
					return this.getItemHandler().getSlotLimit(this.getSlotIndex());
				}
			});
			this.addSlot(new SlotItemHandler(handler, 2, 67, 36) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					return stack.getItem() == ModItems.SUBSPACE_CARDBOARD.get();
				}
			});
			this.addSlot(new OutputSlotItemHandler(handler, 3, 114, 36));
		});
	}
	@Override
	public boolean stillValid(Player player) {
		return stillValid(levelAccess, player, ModBlocks.COMPACT_CRAFTING_TABLE.get());
	}
	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		Slot sourceSlot = slots.get(index);
		if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
		ItemStack sourceStack = sourceSlot.getItem();
		ItemStack copyOfSourceStack = sourceStack.copy();

		// スロット番号の境界線（0~35がプレイヤー、36~39が作業台）
		// ※ addSlotした順序に依存します。今回は プレイヤー(9*3) -> ホットバー(9) -> 作業台(4) の順
		// つまり 0~35: プレイヤー, 36~39: 作業台
		final int VANILLA_FIRST_SLOT_INDEX = 0;
		final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + 36;
		final int TE_INVENTORY_SLOT_COUNT = 4;

		if (index < TE_INVENTORY_FIRST_SLOT_INDEX) {
			// プレイヤーのインベントリ -> 作業台へ移動
			if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
				return ItemStack.EMPTY;
			}
		} else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
			// 作業台 -> プレイヤーのインベントリへ移動
			if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + 36, false)) {
				return ItemStack.EMPTY;
			}
		} else {
			return ItemStack.EMPTY;
		}

		if (sourceStack.getCount() == 0) {
			sourceSlot.set(ItemStack.EMPTY);
		} else {
			sourceSlot.setChanged();
		}
		sourceSlot.onTake(playerIn, sourceStack);
		return copyOfSourceStack;
	}
	private void addPlayerInventory(Inventory playerInventory) {
		for (int i = 0; i < 3; ++i) {
			for (int l = 0; l < 9; ++l) {
				this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
			}
		}
	}
	private void addPlayerHotbar(Inventory playerInventory) {
		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}
	static class OutputSlotItemHandler extends SlotItemHandler {
		public OutputSlotItemHandler(net.minecraftforge.items.IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}
		@Override
		public boolean mayPlace(ItemStack stack) {
			return false; // ここには置けない
		}
	}
	public int getScaledProgress() {
		int progress = this.data.get(0);
		int maxProgress = this.data.get(1);  // Maxは200
		int arrowSize = 24; // 矢印の画像の横幅 (px)

		// 0除算回避 & 比例計算
		return maxProgress != 0 && progress != 0 ? progress * arrowSize / maxProgress : 0;
	}
}