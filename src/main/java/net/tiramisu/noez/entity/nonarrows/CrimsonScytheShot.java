package net.tiramisu.noez.entity.nonarrows;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.tiramisu.noez.item.ProjectileSword;

public class CrimsonScytheShot extends ThrowableProjectile {
    private static final int DURATION = 4;
    private static final float AMPLIFIER = 1.5f;
    private static final float DAMAGE = 4.5f;
    private static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(GrassSpellShot.class, EntityDataSerializers.INT);

    public CrimsonScytheShot(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public CrimsonScytheShot(EntityType<? extends ThrowableProjectile> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        super(pEntityType, pX, pY, pZ, pLevel);
    }

    public CrimsonScytheShot(EntityType<? extends ThrowableProjectile> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (this.getOwner() instanceof LivingEntity livingEntity) {
            ItemStack itemStack = livingEntity.getMainHandItem();
            if (itemStack.getItem() instanceof ProjectileSword){
                if (!this.level().isClientSide()) {
                    Entity target = result.getEntity();
                    if (target instanceof LivingEntity livingTarget) {
                        livingTarget.hurt(livingEntity.damageSources().mobAttack(livingEntity), DAMAGE);
                        this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.0F);
                        this.discard();
                    }
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(LIFESPAN, 0);
    }

    public void setLifespan(int ticks) {
        this.entityData.set(LIFESPAN, ticks);
    }

    @Override
    public void tick() {
        super.tick();
        spawnParticles();
        if (this.tickCount >= this.entityData.get(LIFESPAN)) {
            this.discard();
        }
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity * 0.5F, inaccuracy);
    }

    private void spawnParticles() {
        if (!this.level().isClientSide) return;

    }
}
