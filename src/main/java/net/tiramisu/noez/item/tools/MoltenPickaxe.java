package net.tiramisu.noez.item.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class MoltenPickaxe extends PickaxeItem {

    public MoltenPickaxe(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("noez.molten_pickaxe.tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.molten_pickaxe.tooltip2"));
        pTooltipComponents.add(Component.translatable("noez.molten_pickaxe.tooltip3"));
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity player) {
        if (!level.isClientSide && player instanceof Player && !isBroken(stack) && !((Player) player).getAbilities().instabuild) {
            Block block = state.getBlock();
            Optional<SmeltingRecipe> smeltingRecipe = level.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING, new net.minecraft.world.SimpleContainer(new ItemStack(block.asItem())), level);

            if (smeltingRecipe.isPresent()) {
                ItemStack smeltedResult = smeltingRecipe.get().getResultItem(level.registryAccess());

                if (!smeltedResult.isEmpty()) {
                    Block.popResource(level, pos, smeltedResult.copy());
                    level.destroyBlock(pos, false);
                    stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                    return true;
                }
            }
        }
        return super.mineBlock(stack, level, state, pos, player);
    }


    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (!level.isClientSide && !isBroken(stack) && !player.getAbilities().instabuild) {
            long currentTick = level.getGameTime();
            long lastTick = stack.getOrCreateTag().getLong("LastDurabilityLoss");
            if (currentTick - lastTick >= 100) {
                stack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                stack.getOrCreateTag().putLong("LastDurabilityLoss", currentTick);
            }
        }
    }

        @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.setSecondsOnFire(2);
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    public static boolean isBroken(ItemStack itemStack) {
        return itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1;
    }
}
