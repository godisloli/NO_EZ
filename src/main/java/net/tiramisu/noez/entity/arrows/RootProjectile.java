package net.tiramisu.noez.entity.arrows;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.entity.NoezEntities;

public class RootProjectile extends AbstractArrow {
    private static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(RootProjectile.class, EntityDataSerializers.INT);
    private final float ROOT_PROJECTILE_DAMAGE = 4.0f;
    private final int ROOT_PROJECTILE_DURATION = 40; //ticks
    private final int HEALING_DURATION = 30;

    public RootProjectile (EntityType<? extends RootProjectile> entityType, Level level){
        super(entityType, level);
        this.setNoGravity(true);
    }

    public RootProjectile (Level level, LivingEntity shooter){
        super(NoezEntities.ROOT_PROJECTILE.get(), shooter, level);
        this.setNoGravity(true);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Level level = hitResult.getEntity().level();
        if (!level.isClientSide()) {
            if (hitResult.getEntity() instanceof LivingEntity target && target != this.getOwner()) {
                this.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                        SoundEvents.CHERRY_LEAVES_BREAK, SoundSource.PLAYERS, 2.0F, 1.0F);
                target.hurt(this.damageSources().arrow(this, this.getOwner()), this.ROOT_PROJECTILE_DAMAGE);
                target.addEffect(new MobEffectInstance(NoezEffects.ROOT.get(), ROOT_PROJECTILE_DURATION));
                if (this.getOwner() != null && this.getOwner() instanceof LivingEntity shooter) {
                    shooter.addEffect(new MobEffectInstance(MobEffects.REGENERATION, HEALING_DURATION));
                }
                this.discard();
            }
        }
        super.onHitEntity(hitResult);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LIFESPAN, 1200);
    }

    public void setLifespan(int ticks) {
        this.entityData.set(LIFESPAN, ticks);
    }

    @Override
    public void tick() {
        super.tick();
        setNoPickUp();
        spawnArrowParticles();
        if (this.tickCount >= this.entityData.get(LIFESPAN)) {
            this.discard();
        }
    }

    private void setNoPickUp() {
        this.pickup = Pickup.DISALLOWED;
    }

    private void spawnArrowParticles() {
        if (!this.level().isClientSide) return;
        for (int i = 0; i < 2; i++) {
            this.level().addParticle(
                    ParticleTypes.CHERRY_LEAVES,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    (this.random.nextDouble() - 0.5) * 0.2,
                    (this.random.nextDouble() - 0.5) * 0.2,
                    (this.random.nextDouble() - 0.5) * 0.2
            );
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        this.discard();
    }

}
