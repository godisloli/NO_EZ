package net.tiramisu.noez.item.arsenal;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.item.Critable;
import net.tiramisu.noez.item.LifeStealable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BloodSacrifice extends SwordItem implements Critable, LifeStealable {
    private static final double CRIT_CHANCE = 0.1;
    private static final double CRIT_DAMAGE = 1.5;
    private static final double LIFESTEAL = 0.12;
    private static final float AOE_DAMAGE = 2;
    private boolean ALWAYS_CRIT = false;

    public BloodSacrifice(Tier tier, int Damage, float AttackSpeed, Properties properties){
        super(tier, Damage, AttackSpeed, properties);
    }

    @Override
    public double getCritChance() {
        return CRIT_CHANCE;
    }

    @Override
    public double getCritDamageAmplifier() {
        return CRIT_DAMAGE;
    }

    @Override
    public boolean isAlwaysCrit(){
        return ALWAYS_CRIT;
    }

    @Override
    public void setAlwaysCrit(boolean value){
        this.ALWAYS_CRIT = value;
    }

    @Override
    public double getLifeStealAmount() {
        return LIFESTEAL;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("noez.blood_sacrifice.tooltip1"));
        pTooltipComponents.add(Component.translatable("wip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker.level() instanceof ServerLevel serverLevel) {
            double radius = 1.5;
            var entities = serverLevel.getEntitiesOfClass(LivingEntity.class,
                    pTarget.getBoundingBox().inflate(radius),
                    entity -> entity != pAttacker && entity != pTarget
            );
            for (LivingEntity entity : entities) {
                entity.hurt(pAttacker.damageSources().playerAttack((Player) pAttacker), AOE_DAMAGE);
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
