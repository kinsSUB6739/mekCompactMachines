package com.example.mekcompactmachines.init;
import com.example.mekcompactmachines.MyMekAddon;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MyMekAddon.MODID);
    public static final RegistryObject<Item> SUBSPACE_CARDBOARD = ITEMS.register("subspace_cardboard",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> COMPACT_CRAFTING_TABLE = ITEMS.register("compact_crafting_table",
            () -> new BlockItem(ModBlocks.COMPACT_CRAFTOR.get(), new Item.Properties().stacksTo(64)));
//    public static final RegistryObject<Item> COMPACT_INDUCTION_MATRIX = ITEMS.register("compact_induction_matrix",
//            () -> new BlockItem(ModBlocks.COMPACT_INDUCTION_MATRIX.get(), new Item.Properties().stacksTo(64)));
}