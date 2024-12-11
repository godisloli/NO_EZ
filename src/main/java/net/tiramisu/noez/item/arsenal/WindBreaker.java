package net.tiramisu.noez.item.arsenal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.phys.AABB;
import net.tiramisu.noez.item.Critable;

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

    public WindBreaker(Tier tier, int Damage, float AttackSpeed, Properties properties){
        super(tier, Damage, AttackSpeed, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        boolean result = super.hurtEnemy(pStack, pTarget, pAttacker);
        if (pAttacker instanceof Player) {
            Player player = (Player) pAttacker;
            double fallDistance = player.fallDistance;
            if (fallDistance >= 2) {
                if (fallDistance > 25)
                    fallDistance = 25;
                int bonusDamage = (int) fallDistance + 2;
                pTarget.hurt(player.damageSources().mobAttack(player), bonusDamage);
                if (fallDistance > 10) {
                    double bypassArmorDamage = (fallDistance - 10);
                    float armorPenetration = 0.2f;
                    float finalDamage = (float) bypassArmorDamage * (1 - (pTarget.getArmorValue() * (1 - armorPenetration)) / 20.0f);
                    pTarget.hurt(player.damageSources().mobAttack(player), finalDamage);
                    double radius = 4.0;
                    AABB aoeBox = new AABB(pTarget.getX() - radius, pTarget.getY() - radius, pTarget.getZ() - radius,
                            pTarget.getX() + radius, pTarget.getY() + radius, pTarget.getZ() + radius);
                    List<LivingEntity> nearbyEntities = player.level().getEntitiesOfClass(
                            LivingEntity.class, aoeBox, entity -> entity != player && entity != pTarget && entity.isAlive()
                    );
                    for (LivingEntity entity : nearbyEntities) {
                        entity.hurt(player.damageSources().mobAttack(player), (float) bypassArmorDamage);
                    }
                    if (player.level() instanceof ServerLevel serverLevel){
                        for (int i = 0; i < 50; i++) {
                            double angle = Math.random() * 2 * Math.PI;
                            double distance = Math.random() * radius;
                            double xOffset = Math.cos(angle) * distance;
                            double zOffset = Math.sin(angle) * distance;

                            serverLevel.sendParticles(
                                    net.minecraft.core.particles.ParticleTypes.EXPLOSION,
                                    pTarget.getX() + xOffset,
                                    pTarget.getY() + 1,
                                    pTarget.getZ() + zOffset,
                                    1,
                                    0, 0, 0,
                                    0.1 // Speed multiplier
                            );
                        }
                    }
                }
            }
        }
        return result;
    }
}

