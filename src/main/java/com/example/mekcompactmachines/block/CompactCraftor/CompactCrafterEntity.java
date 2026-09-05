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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

/**
 * Compact Crafter のブロックエンティティクラス。
 * <p>
 * 大容量スロット（最大2048個など）を持つインベントリの管理、およびイベント駆動型の自動クラフトロジック、
 * さらにGUIメニュー（{@link CompactCrafterMenu}）の提供を行います。
 */
public class CompactCrafterEntity extends AbstractCompactMachineEntity {
    private static final int MACHINE_SLOT = CompactCrafterSlot.values().length;

    /**
     * コンストラクタ。
     * 指定された座標と状態を持つ Compact Crafter のブロックエンティティを初期化します。
     *
     * @param pos   ブロックの設置座標
     * @param state ブロックの現在の状態
     */
    public CompactCrafterEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMPACT_CRAFTER.get(), pos, state, MACHINE_SLOT);
    }

    /**
     * 機械内部のインベントリハンドラーを生成します。
     * <p>
     * アイテムの変更を検知した際にイベント駆動で即座にクラフト判定を行う（{@link #tryCraft()}）ほか、
     * {@link CompactCrafterSlot} の定義に基づいてスロットごとの最大スタック数を個別に設定します。
     *
     * @param slotCount スロットの総数
     * @return カスタム設定が適用された ItemStackHandler
     */
    @Override
    protected ItemStackHandler createItemHandler(int slotCount) {
        return new ItemStackHandler(slotCount) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                // イベント駆動：アイテムが置かれたり変化した瞬間に自動でクラフトを試行する
                tryCraft();
            }

            @Override
            public int getSlotLimit(int slot) {
                // スロットインデックスに対応する CompactCrafterSlot を取得して上限値を返す
                CompactCrafterSlot slotEnum = CompactCrafterSlot.fromIndex(slot);
                if (slotEnum != null) {
                    return slotEnum.maxStack();
                }
                return super.getSlotLimit(slot);
            }
        };
    }

    /**
     * ブロックエンティティのデータを追加でNBTタグに保存します（インベントリ状態の永続化）。
     *
     * @param nbt データを保存するCompoundTag
     */
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(nbt);
    }

    /**
     * 保存されたNBTタグからブロックエンティティのデータを読み込みます（インベントリ状態の復元）。
     *
     * @param nbt データを保持しているCompoundTag
     */
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
    }

    /**
     * このブロックエンティティの表示名（タイトルのローカライズ）を取得します。
     *
     * @return 表示名のコンポーネント
     */
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.mek_compact_machines.compact_crafter");
    }

    /**
     * プレイヤーがこのブロックのGUIを開いた際に提供するメニュー（コンテナ）を生成します。
     *
     * @param containerId     コンテナID
     * @param playerInventory プレイヤーのインベントリ
     * @param player          操作しているプレイヤー
     * @return 生成された CompactCrafterMenu
     */
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new CompactCrafterMenu(containerId, playerInventory, this);
    }

    /**
     * クラフトの条件を満たしているかチェックし、条件を満たしている場合は
     * 即座に素材を消費して出力アイテムを生成します（イベント駆動型）。
     */
    public void tryCraft() {
        // CompactCrafterSlot (Enum) のインデックスを使って安全に各スロットのアイテムを取得する
        ItemStack glassStack = itemHandler.getStackInSlot(CompactCrafterSlot.INPUT_1.index());
        ItemStack casingStack = itemHandler.getStackInSlot(CompactCrafterSlot.INPUT_2.index());
        ItemStack cardboardStack = itemHandler.getStackInSlot(CompactCrafterSlot.CARDBOARD.index());
        ItemStack outputStack = itemHandler.getStackInSlot(CompactCrafterSlot.OUTPUT.index());

        // 各素材が必要数揃っているか、および出力先が空またはスタック可能かチェック
        boolean hasGlass = glassStack.getCount() >= CompactCrafterSlot.INPUT_1.maxStack();
        boolean hasCasing = casingStack.getCount() >= CompactCrafterSlot.INPUT_2.maxStack();
        boolean hasCardboard = cardboardStack.getCount() >= CompactCrafterSlot.CARDBOARD.maxStack();
        boolean canOutput = outputStack.isEmpty();

        if (hasGlass && hasCasing && hasCardboard && canOutput) {
            // 素材の消費
            itemHandler.extractItem(CompactCrafterSlot.INPUT_1.index(), CompactCrafterSlot.INPUT_1.maxStack(), false);
            itemHandler.extractItem(CompactCrafterSlot.INPUT_2.index(), CompactCrafterSlot.INPUT_2.maxStack(), false);
            itemHandler.extractItem(CompactCrafterSlot.CARDBOARD.index(), CompactCrafterSlot.CARDBOARD.maxStack(), false);

            // 出力スロットへのアイテム生成
            this.itemHandler.insertItem(CompactCrafterSlot.OUTPUT.index(), new ItemStack(ModItems.SUBSPACE_CARDBOARD.get(), 1), false);
        }
    }
}