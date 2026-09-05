package com.example.mekcompactmachines.block.CompactCraftor;
import com.example.mekcompactmachines.block.AbstractCompactMachineMenu;
import com.example.mekcompactmachines.init.ModBlocks;
import com.example.mekcompactmachines.init.ModItems;
import com.example.mekcompactmachines.init.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
public class CompactCrafterMenu extends AbstractCompactMachineMenu {
	private static final int SLOT_NUM = 4;
	public final CompactCrafterEntity blockEntity;
	private final ContainerLevelAccess levelAccess;
	private final ContainerData data;

	public CompactCrafterMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
		this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
	}

	public CompactCrafterMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
		super(ModMenuTypes.COMPACT_CRAFTER.get(), containerId,SLOT_NUM);
		blockEntity = (CompactCrafterEntity) entity;
		this.levelAccess = ContainerLevelAccess.create(inv.player.level(), entity.getBlockPos());
		this.data = data;
		addDataSlots(data);
		net.minecraftforge.items.IItemHandler handler = blockEntity.getItemHandler();
		addPlayerInventory(inv);
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

	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(levelAccess, player, ModBlocks.COMPACT_CRAFTER.get());
	}

	static class OutputSlotItemHandler extends SlotItemHandler {
		public OutputSlotItemHandler(net.minecraftforge.items.IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}
		@Override
		public boolean mayPlace(ItemStack stack) {
			return false;
		}
	}

	public int getScaledProgress() {
		int progress = this.data.get(0);
		int maxProgress = this.data.get(1);
		int arrowSize = 24;

		return maxProgress != 0 && progress != 0 ? progress * arrowSize / maxProgress : 0;
	}
}