package com.example.mekcompactmachines.client;

import com.example.mekcompactmachines.ModConstants;
import com.example.mekcompactmachines.client.screen.CompactCrafterScreen;
import com.example.mekcompactmachines.init.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * クライアントサイドのイベントや初期化処理を管理するクラス。
 * <p>
 * {@link Dist#CLIENT} を指定しているため、このクラス内の処理はプレイヤーのクライアント環境でのみ実行されます。
 */
@Mod.EventBusSubscriber(modid = ModConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    /**
     * クライアントのセットアップフェーズに呼び出されるイベント。
     * <p>
     * 各種メニュー型（MenuType）に対応するGUI画面（Screen）の登録を行います。
     * スレッドセーフティを考慮し、処理は {@link FMLClientSetupEvent#enqueueWork} を経由して実行されます。
     *
     * @param event クライアントセットアップイベント
     */
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // メニュー（コンテナ）とクライアント側の描画画面（Screen）を紐づける
            MenuScreens.register(ModMenuTypes.COMPACT_CRAFTER.get(), CompactCrafterScreen::new);
        });
    }
}