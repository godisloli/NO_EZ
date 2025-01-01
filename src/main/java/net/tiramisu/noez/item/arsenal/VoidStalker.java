package net.tiramisu.noez.item.arsenal;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.entity.arrows.VoidArrow;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class VoidStalker extends BowItem {
    private static final float PULL_CHANCE = 0.35f;

    public VoidStalker(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("noez.void_stalker.tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.void_stalker.tooltip2"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            ItemStack arrowStack = player.getProjectile(pStack);
            if (!arrowStack.isEmpty() && arrowStack.getItem() instanceof ArrowItem) {
                int charge = this.getUseDuration(pStack) - pTimeLeft;
                float force = getPowerForTime(charge);
                if (force >= 0.1F && !pLevel.isClientSide) {
                    VoidArrow arrow = new VoidArrow(pLevel, player);
                    boolean isPull = pLevel.random.nextFloat() < PULL_CHANCE;
                    if (isPull) {
                        arrow.pull(true);
                    }
                    arrow.setBaseDamage(force * 2F);
                    arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, force * 2.5F, 1.0F);
                    int lifespan = (int) (100 * force);
                    arrow.setLifespan(lifespan);
                    pLevel.addFreshEntity(arrow);
                    pStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(p.getUsedItemHand()));
                    if (!player.getAbilities().instabuild && !arrowStack.isEmpty()) {
                        arrowStack.shrink(1);
                    }
                }
            }
        }
    }
}
