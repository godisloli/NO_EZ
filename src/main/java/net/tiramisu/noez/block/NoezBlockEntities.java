package net.tiramisu.noez.block;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tiramisu.noez.block.vaults.CommonVaultBlockEntity;

public class NoezBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "your_mod_id");

    public static final RegistryObject<BlockEntityType<CommonVaultBlockEntity>> COMMON_VAULT = BLOCK_ENTITIES.register(
            "common_vault",
            () -> BlockEntityType.Builder.of(CommonVaultBlockEntity::new, NoezBlocks.COMMON_VAULT.get()).build(null)
    );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
