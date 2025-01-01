package net.tiramisu.noez.entity.arrows;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.tiramisu.noez.entity.NoezEntities;

import java.util.List;

public class VoidArrow extends AbstractArrow {
    private static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(IridescentArrow.class, EntityDataSerializers.INT);
    private static final float RADIUS = 5f;
    private boolean PULL = false;

    public VoidArrow(Level level, LivingEntity shooter) {
        super(NoezEntities.VOID_ARROW.get(), shooter, level);
    }

    public VoidArrow(EntityType<? extends VoidArrow> type, Level level) {
        super(type, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        applyPullEffect(result.getLocation());
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        applyPullEffect(result.getLocation());
        this.discard();
    }

    private void applyPullEffect(Vec3 hitPos) {
        if (!this.level().isClientSide && PULL) {
            AABB pullArea = new AABB(hitPos.subtract(RADIUS, RADIUS, RADIUS), hitPos.add(RADIUS, RADIUS, RADIUS));
            List<Entity> nearbyEntities = this.level().getEntities(this, pullArea, entity -> entity instanceof LivingEntity);
            for (Entity entity : nearbyEntities) {
                Vec3 direction = hitPos.subtract(entity.position()).normalize();
                double pullStrength = 1;
                Vec3 pullForce = direction.scale(pullStrength);
                entity.setDeltaMovement(entity.getDeltaMovement().add(pullForce));
            }
        }
    }

    public void pull(boolean value) {
        this.PULL = value;
    }

    @Override
    public void tick() {
        super.tick();
        spawnArrowParticles();
    }

    private void spawnArrowParticles() {
        if (!this.level().isClientSide) return;
        for (int i = 0; i < 7; i++) {
            this.level().addParticle(ParticleTypes.REVERSE_PORTAL, this.getX(), this.getY(), this.getZ(),
                    this.getDeltaMovement().x * 0.1, this.getDeltaMovement().y * 0.1, this.getDeltaMovement().z * 0.1);
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    public void setLifespan(int ticks) {
        this.entityData.set(LIFESPAN, ticks);
    }

    private void setNoPickUp() {
        this.pickup = Pickup.DISALLOWED;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LIFESPAN, 120);
    }
}
