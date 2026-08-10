package com.example.mekcompactmachines.init;

import com.example.mekcompactmachines.block.CompactCraftor.CompactCraftorEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "mek_compact_machines");
	public static final RegistryObject<BlockEntityType<CompactCraftorEntity>> COMPACT_CRAFTOR =
			BLOCK_ENTITIES.register("compact_machine_be", () ->
					BlockEntityType.Builder.of(
							CompactCraftorEntity::new,
							ModBlocks.COMPACT_CRAFTOR.get() // ★ModBlocksにあるブロックの変数名と合わせる
					).build(null));

	public static void register(IEventBus eventBus) {
		BLOCK_ENTITIES.register(eventBus);
	}
}