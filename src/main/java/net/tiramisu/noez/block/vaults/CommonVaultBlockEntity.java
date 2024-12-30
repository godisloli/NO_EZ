package net.tiramisu.noez.block.vaults;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.tiramisu.noez.block.NoezBlockEntities;
import net.tiramisu.noez.item.NoezItems;

public class CommonVaultBlockEntity extends BlockEntity {
    private ItemStack storedItem;

    public CommonVaultBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.storedItem = new ItemStack(NoezItems.COMMON_KEY.get());
    }

    public CommonVaultBlockEntity(BlockPos pos, BlockState state) {
        this(NoezBlockEntities.COMMON_VAULT.get(), pos, state);
    }

    public ItemStack getStoredItem() {
        return storedItem;
    }

    public void setStoredItem(ItemStack stack) {
        this.storedItem = stack;
        setChanged();
    }
}

