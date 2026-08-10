package com.example.mekcompactmachines.init;

import com.example.mekcompactmachines.block.CompactCraftor.CompactCraftorMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
	// レジストリの作成 (Mod ID "mek_compact_machines" を指定)
	public static final DeferredRegister<MenuType<?>> MENUS =
			DeferredRegister.create(ForgeRegistries.MENU_TYPES, "mek_compact_machines");

	// メニューの登録
	public static final RegistryObject<MenuType<CompactCraftorMenu>> COMPACT_CRAFTOR =
			registerMenuType("compact_craftor", CompactCraftorMenu::new);

	// ヘルパーメソッド: IForgeMenuTypeを使って登録する定型文
	private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
		return MENUS.register(name, () -> IForgeMenuType.create(factory));
	}

	// メインクラスから呼び出すメソッド
	public static void register(IEventBus eventBus) {
		MENUS.register(eventBus);
	}
}