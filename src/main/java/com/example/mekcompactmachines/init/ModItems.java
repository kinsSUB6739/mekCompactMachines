package com.example.mekcompactmachines.init;

import com.example.mekcompactmachines.ModConstants;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Modで使用するすべてのアイテム（通常アイテムおよびブロックアイテム）を管理・登録するクラス。
 * <p>
 * {@link DeferredRegister} を利用して Forge のアイテムレジストリに登録を行います。
 */
public class ModItems {

    private final static int NON_STACKABLE = 1;
    private static final int MAX_STACK_SIZE = 64;
    /** アイテムのレジストリインスタンス */
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ModConstants.MOD_ID);

    /** サブスペース段ボール（スタック数上限: 1） */
    public static final RegistryObject<Item> SUBSPACE_CARDBOARD =
            ITEMS.register(ModConstants.SUBSPACE_CARDBOARD,
                    () -> new Item(new Item.Properties().stacksTo(NON_STACKABLE)));

    /** Compact Crafter のブロックアイテム（スタック数上限: 64） */
    public static final RegistryObject<Item> COMPACT_CRAFTER =
            ITEMS.register(ModConstants.COMPACT_CRAFTER,
                    () -> new BlockItem(
                            ModBlocks.COMPACT_CRAFTER.get(),
                            new Item.Properties().stacksTo(MAX_STACK_SIZE)));
}