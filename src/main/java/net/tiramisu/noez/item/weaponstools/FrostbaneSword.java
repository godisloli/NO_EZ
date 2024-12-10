package net.tiramisu.noez.item.weaponstools;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.item.Critable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FrostbaneSword extends SwordItem implements Critable {

    private final int FROST_DURATION = 3; //seconds
    private static final double CRIT_CHANCE = 0.15;
    private static final double CRIT_DAMAGE = 1.5;
    private static boolean ALWAYS_CRIT = false;

    public FrostbaneSword(Tier tier, int Damage, float AttackSpeed, Properties properties) {
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
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("noez.frostbane_sword.tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.frostbane_sword.tooltip2"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pTarget.level().isClientSide() && !(pStack.getDamageValue() == pStack.getMaxDamage() - 1)) {
//            pAttacker.level().playSound(
//                    null,
//                    pTarget.getX(),
//                    pTarget.getY(),
//                    pTarget.getZ(),
//                    SoundEvents.PLAYER_HURT_FREEZE, //WIP
//                    SoundSource.PLAYERS,
//                    2.0f,
//                    1.0f
//            );
            if (pTarget.fireImmune() || pTarget.isOnFire()) {
                pTarget.hurt(pAttacker.damageSources().mobAttack(pAttacker), super.getDamage() * 1.5f);
            } else {
                pTarget.addEffect(new MobEffectInstance(NoezEffects.FROSTBITE.get(), FROST_DURATION * 20, 1));
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
