package net.tiramisu.noez.item.weaponstools;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.entity.arrows.IridescentArrow;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IridescentBow extends BowItem {
    public IridescentBow(Item.Properties properties){
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("noez.iridescent_bow.tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.iridescent_bow.tooltip2"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }


    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            ItemStack arrowStack = player.getProjectile(pStack);
            if (!arrowStack.isEmpty() && arrowStack.getItem() instanceof ArrowItem) {
                int charge = this.getUseDuration(pStack) - pTimeLeft;
                float force = getPowerForTime(charge);
                if (force >= 0.1F && !pLevel.isClientSide) {
                    IridescentArrow arrow = new IridescentArrow(pLevel, player);
                    arrow.setBaseDamage(0.1f);
                    arrow.setTrueDamage(force * 3.0F);
                    arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, force * 2.5F, 1.0F);
                    int lifespan = (int) (40 * force);
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