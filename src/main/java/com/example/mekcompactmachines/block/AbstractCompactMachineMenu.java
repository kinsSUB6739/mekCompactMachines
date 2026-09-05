package com.example.mekcompactmachines.block;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Compact Machine の GUI メニュー（コンテナ）の基底となる抽象クラス。
 * <p>
 * プレイヤーのインベントリ配置（メインインベントリおよびホットバー）の自動追加や、
 * Shiftクリック時（クイック移動）のアイテム移動ロジックを共通提供します。
 */
public abstract class AbstractCompactMachineMenu extends AbstractContainerMenu {

    /**
     * 機械側が持つスロットの総数
     */
    private final int MACHINE_SLOT_NUM;

    /**
     * コンストラクタ。
     *
     * @param menuType         メニューの型（MenuType）
     * @param containerId      コンテナID
     * @param machineSlotCount 機械側が持つインベントリスロットの数
     */
    protected AbstractCompactMachineMenu(@Nullable MenuType<?> menuType, int containerId, int machineSlotCount) {
        super(menuType, containerId);
        this.MACHINE_SLOT_NUM = machineSlotCount;
    }

    /**
     * プレイヤーのインベントリ（メイン 27スロット ＋ ホットバー 9スロット）をGUIの下部に配置します。
     *
     * @param playerInventory プレイヤーのインベントリ
     */
    protected void addPlayerInventory(Inventory playerInventory) {
        // メインインベントリ（3段×9列）の配置
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
        // ホットバー（1段×9列）の配置
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    /**
     * プレイヤーがスロットを Shift+クリック（クイック移動）した際のアイテム移動処理を定義します。
     *
     * @param playerIn 操作しているプレイヤー
     * @param index    クリックされたスロットのインデックス
     * @return 移動に成功した場合は移動後のItemStack、失敗した場合は {@link ItemStack#EMPTY}
     */
    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // インデックスの範囲定義（バニラ: 0〜35、機械側インベントリ: 36〜想定）
        final int PLAYER_SLOT_INDEX = 0;
        final int MACHINE_SLOT_INDEX = 36;

        // 1. プレイヤーインベントリ側から機械側へアイテムを移す場合
        if (index < MACHINE_SLOT_INDEX) {
            if (!moveItemStackTo(sourceStack, MACHINE_SLOT_INDEX, MACHINE_SLOT_INDEX + MACHINE_SLOT_NUM, false)) {
                return ItemStack.EMPTY;
            }
        }
        // 2. 機械側からプレイヤーインベントリ側へアイテムを移す場合
        else if (index < MACHINE_SLOT_INDEX + MACHINE_SLOT_NUM) {
            if (!moveItemStackTo(sourceStack, PLAYER_SLOT_INDEX, MACHINE_SLOT_INDEX, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        // スタック数が0になった場合の処理
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }
}