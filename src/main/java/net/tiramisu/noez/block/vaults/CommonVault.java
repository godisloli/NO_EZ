package net.tiramisu.noez.block.vaults;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.item.NoezItems;

import javax.annotation.Nullable;
import java.util.List;

public class CommonVault extends Block implements EntityBlock {

    public static final IntegerProperty VAULT_STATE = IntegerProperty.create("vault_state", 0, 2);
    public static final ResourceLocation COMMON_VAULT_LOOT_TABLE = new ResourceLocation(NOEZ.MOD_ID, "blocks/common_vault");

    public CommonVault(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(VAULT_STATE, 0));
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack handItem = pPlayer.getItemInHand(pHand);

        if (!pLevel.isClientSide && pState.getValue(VAULT_STATE) == 0 && handItem.getItem() == NoezItems.COMMON_KEY.get()) {
            if (!pPlayer.getAbilities().instabuild) {
                handItem.shrink(1);
            }

            pLevel.setBlock(pPos, pState.setValue(VAULT_STATE, 1), 3);

            if (pLevel instanceof ServerLevel serverLevel) {
                serverLevel.scheduleTick(pPos, this, 3 * 20);
            }

            pLevel.playSound(null, pPos, SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        int currentState = pState.getValue(VAULT_STATE);

        if (currentState == 1) {
            pLevel.setBlock(pPos, pState.setValue(VAULT_STATE, 2), 3);

            pLevel.playSound(null, pPos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0f, 1.2f);
            pLevel.gameEvent(null, GameEvent.BLOCK_ACTIVATE, pPos);

            pLevel.scheduleTick(pPos, this, 20);
        } else if (currentState == 2) {
            LootTable lootTable = pLevel.getServer().getLootData().getLootTable(COMMON_VAULT_LOOT_TABLE);
            LootParams.Builder lootParams = new LootParams.Builder(pLevel)
                    .withParameter(LootContextParams.ORIGIN, pPos.getCenter())
                    .withOptionalParameter(LootContextParams.BLOCK_STATE, pState)
                    .withOptionalParameter(LootContextParams.TOOL, ItemStack.EMPTY);;
            List<ItemStack> loot = lootTable.getRandomItems(lootParams.create(LootContextParamSets.BLOCK));

            for (ItemStack itemStack : loot) {
                Block.popResource(pLevel, pPos, itemStack);
            }

            pLevel.setBlock(pPos, pState.setValue(VAULT_STATE, 0), 3);

            pLevel.playSound(null, pPos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1.0f, 1.2f);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(VAULT_STATE);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isOcclusionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CommonVaultBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
