package net.tiramisu.noez.item.arsenal;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.item.Critable;
import net.tiramisu.noez.item.LifeStealable;
import net.tiramisu.noez.particles.NoezParticles;
import net.tiramisu.noez.sound.NoezSounds;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrimsonSoulEater extends SwordItem implements Critable, LifeStealable {
    private static final double CRIT_CHANCE = 0.15;
    private static final double CRIT_DAMAGE = 1.5;
    private static final double LIFESTEAL = 0.08;
    private static final float BONUS_DAMAGE_AMPLIFIER = 0.2f;
    private boolean ALWAYS_CRIT = false;

    public CrimsonSoulEater(Tier tier, int Damage, float AttackSpeed, Properties properties) {
        super(tier, Damage, AttackSpeed, properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("noez.crimson_soul_eater.tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.crimson_soul_eater.tooltip2"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.getHealth() < target.getMaxHealth() && !isBroken(stack)) {
            if (target.getHealth() < target.getMaxHealth() * 0.1) {
                target.kill();
                attacker.heal(target.getMaxHealth() * 0.1f);
                if (attacker.level() instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < 20; i++)
                        serverLevel.sendParticles(
                                NoezParticles.BLEED.get(),
                                target.getX(),
                                target.getY(),
                                target.getZ(),
                                5,
                                0,0.5,0,
                                0.2
                        );
                    serverLevel.playSound(
                            null,
                            target.getX(),
                            target.getY(),
                            target.getZ(),
                            NoezSounds.EXECUTION.get(),
                            SoundSource.NEUTRAL,
                            0.2f,
                            1f
                    );
                }
            }
            target.hurt(attacker.damageSources().mobAttack(attacker), (target.getMaxHealth() - target.getHealth()) * BONUS_DAMAGE_AMPLIFIER);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    public boolean isBroken(ItemStack itemStack) {
        return itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1;
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
}
