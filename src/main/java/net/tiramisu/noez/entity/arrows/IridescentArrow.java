package net.tiramisu.noez.entity.arrows;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.tiramisu.noez.entity.NoezEntities;
import net.tiramisu.noez.particles.NoezParticles;

public class IridescentArrow extends AbstractArrow {
    private static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(IridescentArrow.class, EntityDataSerializers.INT);
    private float IRIDESCENT_DAMAGE = 2.0f;
    private int PIERCING_NUMBER = 10;

    public IridescentArrow(EntityType<? extends IridescentArrow> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
        this.setPierceLevel((byte) PIERCING_NUMBER);
    }

    public IridescentArrow(Level level, LivingEntity shooter) {
        super(NoezEntities.IRIDESCENT_ARROW.get(), shooter, level);
        this.setNoGravity(true);
        this.setPierceLevel((byte) PIERCING_NUMBER);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LIFESPAN, 120);
    }

    public void setLifespan(int ticks) {
        this.entityData.set(LIFESPAN, ticks);
    }

    @Override
    public void tick() {
        super.tick();
        spawnArrowParticles();
        if (this.tickCount >= this.entityData.get(LIFESPAN)) {
            this.discard();
        }
    }

    public void setTrueDamage(float damage){
        this.IRIDESCENT_DAMAGE = damage;
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity target) {
            if (target instanceof Player player)
                if (player.isCreative()) {
                    super.onHitEntity(hitResult);
                    return;
                }
            target.hurt(this.damageSources().generic(), 0.0f);
            try {
                float newHealth = target.getHealth() - IRIDESCENT_DAMAGE;
                if (newHealth < IRIDESCENT_DAMAGE) {
                    target.setHealth(0.0f);
                    this.discard();
                } else {
                    target.setHealth(newHealth);
                }
            }
            catch (Exception exception){
                return;
            }
        }
        super.onHitEntity(hitResult);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    private void spawnArrowParticles() {
        if (!this.level().isClientSide) return;
        for (int i = 0; i < 2; i++) {
            this.level().addParticle(NoezParticles.BUTTERFLY.get(), this.getX(), this.getY(), this.getZ(),
                    this.getDeltaMovement().x * 0.1, this.getDeltaMovement().y * 0.1, this.getDeltaMovement().z * 0.1);
        }
    }
}

