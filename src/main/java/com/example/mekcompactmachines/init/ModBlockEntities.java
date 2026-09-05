package com.example.mekcompactmachines.init;

import com.example.mekcompactmachines.ModConstants;
import com.example.mekcompactmachines.block.CompactCraftor.CompactCrafterEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Modで使用するすべてのブロックエンティティ（BlockEntity）を管理・登録するクラス。
 * <p>
 * {@link DeferredRegister} を利用して Forge のブロックエンティティタイプレジストリに登録を行います。
 */
public class ModBlockEntities {

	/** ブロックエンティティのレジストリインスタンス */
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ModConstants.MOD_ID);

	/** Compact Crafter のブロックエンティティタイプ */
	public static final RegistryObject<BlockEntityType<CompactCrafterEntity>> COMPACT_CRAFTER =
			BLOCK_ENTITIES.register(ModConstants.COMPACT_CRAFTER, () ->
					BlockEntityType.Builder.of(
							CompactCrafterEntity::new,
							ModBlocks.COMPACT_CRAFTER.get()
					).build(null));

	/**
	 * メインクラスから呼び出し、Forgeのモディベントバスにレジストリを登録します。
	 *
	 * @param eventBus Mod専用のイベントバス
	 */
	public static void register(IEventBus eventBus) {
		BLOCK_ENTITIES.register(eventBus);
	}
}