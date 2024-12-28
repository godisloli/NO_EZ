package net.tiramisu.noez.entity.nonarrows;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.tiramisu.noez.item.NoezItems;
import net.tiramisu.noez.particles.NoezParticles;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ThrownSoulPearl extends ThrownEnderpearl {

    public ThrownSoulPearl(EntityType<? extends ThrownEnderpearl> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected @NotNull Item getDefaultItem() {
        return NoezItems.SOUL_PEARL.get();
    }

    protected void onHitEntity(@NotNull EntityHitResult pResult) {
        super.onHitEntity(pResult);
        pResult.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);

        for (int i = 0; i < 32; ++i) {
            this.level().addParticle(NoezParticles.BUTTERFLY.get(), this.getX(), this.getY() + this.random.nextDouble() * (double)2.0F, this.getZ(), this.random.nextGaussian(), 0.0F, this.random.nextGaussian());
        }

        if (!this.level().isClientSide && !this.isRemoved()) {
            Entity entity = this.getOwner();
            if (entity instanceof ServerPlayer serverplayer) {
                if (serverplayer.connection.isAcceptingMessages() && serverplayer.level() == this.level() && !serverplayer.isSleeping()) {
                    EntityTeleportEvent.EnderPearl event = ForgeEventFactory.onEnderPearlLand(serverplayer, this.getX(), this.getY(), this.getZ(), this, 0.0F, pResult);
                    if (!event.isCanceled()) {
                        if (entity.isPassenger()) {
                            serverplayer.dismountTo(this.getX(), this.getY(), this.getZ());
                        } else {
                            entity.teleportTo(this.getX(), this.getY(), this.getZ());
                        }
                        entity.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                        entity.resetFallDistance();
                    }
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
