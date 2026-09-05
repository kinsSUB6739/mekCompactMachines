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

/**
 * Compact Machine のブロックエンティティ（BlockEntity）の基底となる抽象クラス。
 * <p>
 * インベントリの保持、GUIメニュー（{@link MenuProvider}）の提供、および
 * 外部Mod（パイプやホッパー等）からのアイテム搬出入を可能にする
 * Capability（ItemHandler）のライフサイクル管理を共通提供します。
 */
public abstract class AbstractCompactMachineEntity extends BlockEntity implements MenuProvider {

    /**
     * 機械内部のインベントリを管理するハンドラー
     */
    protected final ItemStackHandler itemHandler;

    /**
     * 外部システムにインベントリ機能を提供するための遅延オプショナル（Capability用）
     */
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    /**
     * インベントリハンドラーを生成するメソッド。
     * サブクラスでオーバーライドすることで、スロットのスタック制限などを個別にカスタマイズできます。
     *
     * @param slotCount スロットの総数
     * @return 生成された ItemStackHandler
     */
    protected ItemStackHandler createItemHandler(int slotCount) {
        return new ItemStackHandler(slotCount) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    /**
     * コンストラクタ。
     *
     * @param type      ブロックエンティティのタイプ
     * @param pos       ブロックの設置座標
     * @param state     ブロックの状態
     * @param slotCount 機械が持つインベントリスロットの総数
     */
    // コンストラクタを次のように修正
    public AbstractCompactMachineEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int slotCount) {
        super(type, pos, state);
        this.itemHandler = this.createItemHandler(slotCount);
    }

    /**
     * 内部のインベントリハンドラーを取得します（GUIメニュー等で使用）。
     *
     * @return インベントリハンドラー
     */
    public IItemHandler getItemHandler() {
        return this.itemHandler;
    }

    /**
     * 外部からの Capability 要求（アイテム搬出入など）を受け取った際に呼び出されます。
     *
     * @param cap  要求されたCapabilityの種類
     * @param side アクセスされている面の方向
     * @param <T>  Capabilityの型
     * @return 該当するCapabilityのインスタンス（ラップされたLazyOptional）
     */
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    /**
     * ブロックエンティティがワールドにロードされた（設置・読み込みされた）際に呼び出されます。
     * Capability のインスタンスを初期化します。
     */
    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    /**
     * ブロックエンティティが破棄される際に呼び出されます。
     * メモリリークを防ぐため、Capability のオプショナルを無効化（invalidate）します。
     */
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }
}