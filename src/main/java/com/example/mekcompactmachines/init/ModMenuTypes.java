package com.example.mekcompactmachines.init;

import com.example.mekcompactmachines.ModConstants;
import com.example.mekcompactmachines.block.CompactCraftor.CompactCrafterMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Modで使用するコンテナメニュー（GUI）の型（MenuType）を管理・登録するクラス。
 * <p>
 * {@link DeferredRegister} を利用して Forge のメニューレジストリに登録を行います。
 */
public class ModMenuTypes {

    /** GUI（メニュー）のレジストリインスタンス */
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ModConstants.MOD_ID);

    /** Compact Crafter のGUIメニュー型 */
    public static final RegistryObject<MenuType<CompactCrafterMenu>> COMPACT_CRAFTER =
            registerMenuType(ModConstants.COMPACT_CRAFTER, CompactCrafterMenu::new);

    /**
     * ネットワーク経由でのデータ同期（FriendlyByteBuf等）を伴うカスタムメニュー型を登録するためのヘルパーメソッド。
     *
     * @param name    登録するメニューの識別子名
     * @param factory クライアント側・サーバー側でのメニュー生成を行うファクトリー
     * @param <T>     登録するメニューの型（AbstractContainerMenuのサブクラス）
     * @return 登録されたメニュー型のレジストリオブジェクト
     */
    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    /**
     * メインクラスから呼び出し、Forgeのモディベントバスにレジストリを登録します。
     *
     * @param eventBus Mod専用のイベントバス
     */
    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}