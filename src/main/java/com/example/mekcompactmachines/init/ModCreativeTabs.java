package com.example.mekcompactmachines.init;

import com.example.mekcompactmachines.MyMekAddon;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MyMekAddon.MODID);

    public static final RegistryObject<CreativeModeTab> ADDON_TAB = CREATIVE_TABS.register("addon_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.SUBSPACE_CARDBOARD.get()))
                    .title(Component.translatable("creativetab.mek_compact_machines"))
                    .displayItems((parameters, output) -> {
                        // アイテムを並べる順番を指定
                        output.accept(ModItems.SUBSPACE_CARDBOARD.get());       // 段ボール
                        output.accept(ModItems.COMPACT_CRAFTER.get());   // 作業台 (追加)
//                        output.accept(ModItems.COMPACT_INDUCTION_MATRIX.get()); // マトリックス (追加)
                    })
                    .build());
}