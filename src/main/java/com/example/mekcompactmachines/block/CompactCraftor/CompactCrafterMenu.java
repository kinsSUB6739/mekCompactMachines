package com.example.mekcompactmachines.block.CompactCraftor;

import com.example.mekcompactmachines.block.AbstractCompactMachineMenu;
import com.example.mekcompactmachines.init.ModBlocks;
import com.example.mekcompactmachines.init.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

/**
 * Compact Crafter のGUIメニュー（コンテナ）クラス。
 * <p>
 * プレイヤーのインベントリと Compact Crafter の専用スロット（{@link CompactCrafterSlot}）を紐付け、
 * スロットごとの搬入制限や最大スタック数の制御を行います。
 */
public class CompactCrafterMenu extends AbstractCompactMachineMenu {

    /**
     * 機械側が持つスロットの総数
     */
    private static final int SLOT_NUM = 4;

    /**
     * 対象となる Compact Crafter のブロックエンティティ
     */
    public final CompactCrafterEntity blockEntity;

    /**
     * ブロックとの距離や存在を判定するためのアクセサ
     */
    private final ContainerLevelAccess levelAccess;

    /**
     * クライアント側（サーバーからパケットを受け取ってGUIを開く際）に呼び出されるコンストラクタ。
     *
     * @param containerId コンテナID
     * @param inv         プレイヤーのインベントリ
     * @param extraData   ブロックの座標情報などを含むバッファ
     */
    public CompactCrafterMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    /**
     * サーバー側およびメインで呼び出されるコンストラクタ。
     *
     * @param containerId コンテナID
     * @param inv         プレイヤーのインベントリ
     * @param entity      対応するブロックエンティティ
     */
    public CompactCrafterMenu(int containerId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.COMPACT_CRAFTER.get(), containerId, SLOT_NUM);
        this.blockEntity = (CompactCrafterEntity) entity;
        this.levelAccess = ContainerLevelAccess.create(inv.player.level(), entity.getBlockPos());

        IItemHandler handler = blockEntity.getItemHandler();

        // プレイヤーのインベントリをGUIに配置
        addPlayerInventory(inv);

        for (CompactCrafterSlot slot : CompactCrafterSlot.list()) {
            this.addSlot(makeSlot(handler, slot));
        }
    }

    /**
     * {@link CompactCrafterSlot} の定義情報に基づいて、専用のインベントリスロットを動的に生成します。
     *
     * @param handler  機械のインベントリハンドラー
     * @param slotInfo スロットの定義情報（Enum）
     * @return 生成された SlotItemHandler
     */
    private SlotItemHandler makeSlot(IItemHandler handler, CompactCrafterSlot slotInfo) {
        return new SlotItemHandler(handler, slotInfo.index(), slotInfo.xPos(), slotInfo.yPos()) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                // 出力スロットの場合はアイテムを配置できないようにする
                if (slotInfo.isOutput()) {
                    return false;
                }
                ResourceLocation regName = ForgeRegistries.ITEMS.getKey(stack.getItem());
                return regName != null && slotInfo.itemName().equals(regName.toString());
            }

            @Override
            public int getMaxStackSize(@NotNull ItemStack stack) {
                return slotInfo.maxStack();
            }
        };
    }

    /**
     * プレイヤーがこのGUIを開き続けても安全か（ブロックが破壊されたり遠く離れていないか）を判定します。
     *
     * @param player 操作しているプレイヤー
     * @return 妥当な場合は true、そうでない場合は false（自動でGUIが閉じます）
     */
    @Override
    public boolean stillValid(Player player) {
        return stillValid(levelAccess, player, ModBlocks.COMPACT_CRAFTER.get());
    }
}