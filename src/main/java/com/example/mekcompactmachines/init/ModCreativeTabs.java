package com.example.mekcompactmachines.init;

import com.example.mekcompactmachines.ModConstants;
import com.example.mekcompactmachines.MyMekAddon;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Modで使用するクリエイティブタブを管理・登録するクラス。
 * <p>
 * {@link DeferredRegister} を利用して Forge のクリエイティブタブレジストリに登録を行います。
 */
public class ModCreativeTabs {

    /** クリエイティブタブのレジストリインスタンス */
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModConstants.MOD_ID);

    /** 当Mod専用のクリエイティブタブ（アイコン: サブスペース段ボール） */
    public static final RegistryObject<CreativeModeTab> ADDON_TAB = CREATIVE_TABS.register(ModConstants.TAB_ADDON,
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.SUBSPACE_CARDBOARD.get()))
                    .title(Component.translatable("creativetab.mek_compact_machines"))
                    .displayItems((parameters, output) -> {
                        // タブ内に表示するアイテムと、その並び順を指定
                        output.accept(ModItems.SUBSPACE_CARDBOARD.get());
                        output.accept(ModItems.COMPACT_CRAFTER.get());
                    })
                    .build());
}