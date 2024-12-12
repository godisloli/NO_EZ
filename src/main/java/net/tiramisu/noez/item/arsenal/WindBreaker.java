package net.tiramisu.noez.item.arsenal;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.item.Critable;
import net.tiramisu.noez.particles.NoezParticles;
import net.tiramisu.noez.sound.NoezSounds;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WindBreaker extends SwordItem implements Critable {
    private static final double CRIT_CHANCE = 0.15;
    private static final double CRIT_DAMAGE = 1.5;
    private boolean ALWAYS_CRIT = false;

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
        pTooltipComponents.add(Component.translatable("wip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public WindBreaker(Tier tier, int Damage, float AttackSpeed, Properties properties){
        super(tier, Damage, AttackSpeed, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        boolean result = super.hurtEnemy(pStack, pTarget, pAttacker);
        if (pAttacker instanceof Player player && player.level() instanceof ServerLevel serverLevel) {
            double fallDistance = player.fallDistance;
            if (fallDistance > 2) {
                int bonusDamage = Math.min((int) fallDistance + 2, 25);
                if (fallDistance > 10) {
                    serverLevel.playSound(
                            null,
                            pTarget.getX(),
                            pTarget.getY(),
                            pTarget.getZ(),
                            NoezSounds.WINDBREAKER_SLAM.get(),
                            SoundSource.PLAYERS,
                            1f,
                            1f
                    );
                    double AoeDamage = (fallDistance - 10);
                    pTarget.addEffect(new MobEffectInstance(NoezEffects.ARMOR_CRUNCH.get(), (int) AoeDamage * 20 + 180, 3));
                    double radius = 3.0;
                    AABB aoeBox = new AABB(
                            pTarget.getX() - radius, pTarget.getY() - radius, pTarget.getZ() - radius,
                            pTarget.getX() + radius, pTarget.getY() + radius, pTarget.getZ() + radius
                    );
                    List<LivingEntity> nearbyEntities = player.level().getEntitiesOfClass(
                            LivingEntity.class, aoeBox, entity -> entity != player && entity != pTarget && entity.isAlive()
                    );
                    for (LivingEntity entity : nearbyEntities) {
                        entity.hurt(player.damageSources().mobAttack(player), (float) AoeDamage);
                    }
                    for (int i = 0; i < 20; i++) {
                        double angle = Math.random() * 2 * Math.PI;
                        double distance = Math.random() * (radius + 1);
                        double xOffset = Math.cos(angle) * distance;
                        double zOffset = Math.sin(angle) * distance;
                        serverLevel.sendParticles(
                                NoezParticles.WINDBLOW.get(),
                                pTarget.getX() + xOffset,
                                pTarget.getY() + 0.5,
                                pTarget.getZ() + zOffset,
                                1,
                                0, 0, 0,
                                0
                        );
                    }
                } else {
                    serverLevel.playSound(
                            null,
                            pTarget.getX(),
                            pTarget.getY(),
                            pTarget.getZ(),
                            NoezSounds.WINDBREAKER_HIT.get(),
                            SoundSource.PLAYERS,
                            1f,
                            1f
                    );
                }
                pTarget.hurt(player.damageSources().mobAttack(player), bonusDamage);
                player.resetFallDistance();
            }
        }
        return result;
    }
}

