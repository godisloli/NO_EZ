package net.tiramisu.noez.item.weaponstools;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HellfireSword extends SwordItem {
    public HellfireSword(Tier tier, int Damage, float AttackSpeed, Properties properties){
        super(tier, Damage, AttackSpeed, properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("noez.hellfire_sword.tooltip1"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if(!pTarget.level().isClientSide())
        {
            pTarget.setSecondsOnFire(4);
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
