package com.example.mekcompactmachines.block;

import com.example.mekcompactmachines.ModConstants;
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

/**
 * Compact Machine ブロックの基底（ベース）となる抽象クラス。
 * <p>
 * このクラスを継承することで、以下の機能が自動的にサポートされます。
 * <ul>
 *     <li>プレイヤーの右クリックによる GUI（メニュー）の展開</li>
 *     <li>Mekanism の Configurator（設定器）等を使ったスニーク＋右クリックによる即時撤去</li>
 *     <li>ブロック破壊時に、内部のインベントリ（アイテム）をワールドにドロップする処理</li>
 * </ul>
 */
public abstract class AbstractCompactMachineBlock extends Block implements EntityBlock {

    /**
     * コンストラクタ。
     *
     * @param properties ブロックの硬さや材質などの基本プロパティ
     */
    public AbstractCompactMachineBlock(Properties properties) {
        super(properties);
    }

    /**
     * プレイヤーがブロックに対して右クリック（インタラクト）した際に呼び出されるメソッド。
     *
     * @param state  対象ブロックの現在の状態
     * @param level  対象ブロックが存在するワールド（レベル）
     * @param pos    対象ブロックの座標
     * @param player インタラクトしたプレイヤー
     * @param hand   プレイヤーが使用した手（メインハンド / オフハンド）
     * @param hit    クリックした面や位置の詳細情報
     * @return インタラクションの結果（成功したか、処理をパスしたか等）
     */
    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);

        // 手に持っているアイテムが「configurator（設定器）」かどうかを判定
        boolean isConfigurator = heldItem.getItem().getDescriptionId().contains(ModConstants.CONFIGURATOR);

        if (!level.isClientSide) {
            // サーバーサイドでの処理

            // スニーク（しゃがみ）状態 ＋ Configurator所持 の場合、ブロックをアイテム化して撤去
            if (player.isCrouching() && isConfigurator) {
                level.destroyBlock(pos, true, player);
                return InteractionResult.SUCCESS;
            }

            // Configurator以外のアイテムを持っている（または素手）の場合、GUIを開く
            if (!isConfigurator) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof AbstractCompactMachineEntity) {
                    NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) be, pos);
                }
            }
        }

        // クライアントサイドでも、スニーク ＋ Configurator の場合は腕を振るモーション等を成功させる
        if (player.isCrouching() && isConfigurator) return InteractionResult.SUCCESS;

        // クライアント側なら SUCCESS を返し、サーバー側ならそれに準じた結果を返す
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /**
     * ブロックが破壊されたり、別のブロックに置き換わったりした際に呼び出されるメソッド。
     * 主に、内部のインベントリをワールドにドロップするために使用します。
     *
     * @param state    変更前のブロックの状態
     * @param level    ワールド（レベル）
     * @param pos      ブロックの座標
     * @param newState 変更後のブロックの状態
     * @param isMoving ピストンなどによってブロックが移動しているかどうか
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        // ブロックそのものが別の種類に変わった場合のみ処理する（状態変化のみの場合は無視）
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide) {
                BlockEntity blockEntity = level.getBlockEntity(pos);

                // 内部にアイテムを保持している場合、全てワールドにドロップする
                if (blockEntity instanceof AbstractCompactMachineEntity machineEntity) {
                    net.minecraftforge.items.IItemHandler itemHandler = machineEntity.getItemHandler();

                    if (itemHandler != null) {
                        for (int i = 0; i < itemHandler.getSlots(); i++) {
                            ItemStack stack = itemHandler.getStackInSlot(i);
                            if (!stack.isEmpty()) {
                                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                            }
                        }
                    }

                    // コンパレーターなどのレッドストーン出力を更新
                    level.updateNeighbourForOutputSignal(pos, this);
                }
            }
            // 親クラス（Block）の削除処理を呼び出す
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}