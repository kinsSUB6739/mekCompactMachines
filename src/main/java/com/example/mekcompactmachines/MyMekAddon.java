package com.example.mekcompactmachines;

import com.example.mekcompactmachines.init.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Mekanism Compact Machines アドオンのメインエントリポイントとなるクラス。
 * <p>
 * {@link ModConstants#MOD_ID} をModの識別子として登録し、
 * コンストラクタ内で各種アイテム、ブロック、ブロックエンティティ、クリエイティブタブ、
 * およびGUIメニューのレジストリを Forge のモディベントバスに登録します。
 */
@Mod(ModConstants.MOD_ID)
public class MyMekAddon {

    /**
     * メインクラスのコンストラクタ。
     * Modがロードされる際に Forge によって自動的に呼び出されます。
     *
     * @param context ForgeのModローディングコンテキスト。Mod専用のイベントバスを取得するために使用します。
     */
    public MyMekAddon(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // 各種要素のレジストリをイベントバスに登録
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);
        ModMenuTypes.register(modEventBus);
    }
}