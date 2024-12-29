package net.tiramisu.noez.entity.nonarrows;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.tiramisu.noez.entity.NoezEntities;
import net.tiramisu.noez.item.NoezItems;
import net.tiramisu.noez.particles.NoezParticles;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

public class ThrownSoulPearl extends ThrowableItemProjectile {

    private static final float AOE_DAMAGE = 6.5f;
    private static final double AEO_RADIUS = 4;

    public ThrownSoulPearl(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ThrownSoulPearl(Level level) {
        super(NoezEntities.SOUL_PEARL_ENTITY.get(), level);
    }

    public ThrownSoulPearl(Level level, LivingEntity livingEntity) {
        super(NoezEntities.SOUL_PEARL_ENTITY.get(), livingEntity, level);
    }

    protected @NotNull Item getDefaultItem() {
        return NoezItems.SOUL_PEARL.get();
    }

    protected void onHitEntity(@NotNull EntityHitResult pResult) {
        super.onHitEntity(pResult);
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);

        if (this.level() instanceof ServerLevel serverLevel) {
            Random random = new Random();
            int particleCount = 80;
            double radius = 1;
            for (int i = 0; i < particleCount; i++) {
                double xOffset = (random.nextDouble() * 2 - 1) * radius;
                double yOffset = 0.0f;
                double zOffset = (random.nextDouble() * 2 - 1) * radius;

                double xVel = random.nextDouble() + 0.2;
                double zVel = random.nextDouble() + 0.2;
                double yVel = random.nextDouble() * 0.5 + 0.5;
                if (random.nextBoolean()) {
                    yVel = -yVel;
                }
                Vec3 particlePos = new Vec3(this.getX() + xOffset, this.getY() + yOffset + 1.0, this.getZ() + zOffset);
                serverLevel.sendParticles(
                        NoezParticles.BUTTERFLY.get(),
                        particlePos.x(), particlePos.y(), particlePos.z(),
                        1,
                        xVel, yVel, zVel,
                        0.1
                );
            }
            var entities = serverLevel.getEntitiesOfClass(LivingEntity.class,
                    this.getBoundingBox().inflate(AEO_RADIUS),
                    entity -> entity != this.getOwner()
            );
            for (LivingEntity entity : entities) {
                entity.hurt(this.getOwner().damageSources().magic(), AOE_DAMAGE);
            }
        }

        if (!this.level().isClientSide && !this.isRemoved()) {
            Entity entity = this.getOwner();
            if (entity instanceof ServerPlayer serverplayer) {
                if (serverplayer.connection.isAcceptingMessages() && serverplayer.level() == this.level() && !serverplayer.isSleeping()) {
                    if (entity.isPassenger()) {
                        serverplayer.dismountTo(this.getX(), this.getY(), this.getZ());
                    } else {
                        entity.teleportTo(this.getX(), this.getY(), this.getZ());
                    }
                    entity.resetFallDistance();
                }
            } else if (entity != null) {
                entity.teleportTo(this.getX(), this.getY(), this.getZ());
                entity.resetFallDistance();
            }
            this.discard();
        }
    }

    public void tick() {
        Entity entity = this.getOwner();
        if (entity instanceof Player && !entity.isAlive()) {
            this.discard();
        } else {
            super.tick();
        }
    }

    @Nullable
    public Entity changeDimension(@NotNull ServerLevel serverLevel, @NotNull ITeleporter teleporter) {
        Entity entity = this.getOwner();
        if (entity != null && entity.level().dimension() != serverLevel.dimension()) {
            this.setOwner(null);
        }
        return super.changeDimension(serverLevel, teleporter);
    }
}
