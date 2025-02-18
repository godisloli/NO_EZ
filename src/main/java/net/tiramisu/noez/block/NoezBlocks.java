package net.tiramisu.noez.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.block.vaults.CommonVault;
import net.tiramisu.noez.item.NoezItems;

import java.util.function.Supplier;

public class NoezBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, NOEZ.MOD_ID);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, final Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject <T> block) {
        return NoezItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static final RegistryObject<Block> COMMON_VAULT = registerBlock("common_vault",
            () -> new CommonVault(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN)
                    .strength(50.0f, 1200.0f)
                    .noOcclusion()
                    .lightLevel(state -> 0)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
