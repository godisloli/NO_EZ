package net.tiramisu.noez.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.event.LineOfSight;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.item.weaponstools.NaturaBlade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }
    @Inject(at = @At("HEAD"), method = "hasLineOfSight", cancellable = true)
    void isLookingAtMe(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        if (entity instanceof LivingEntity) {
            if (!LineOfSight.isLookingAtYou(livingEntity, (LivingEntity) entity)) {
                if (entity instanceof IronGolem || entity instanceof EnderDragon || entity instanceof WitherBoss || entity instanceof Warden)
                    cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "checkFallDamage", at = @At("HEAD"), cancellable = true)
    public void RelocatorNoFallDamage(double heightDifference, boolean onGround, BlockState blockState, BlockPos pos, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        Level world = entity.level();
        if (entity instanceof Player player) {
            long lastTeleportTime = player.getPersistentData().getLong("RelocatorTeleportTime");
            if (world.getGameTime() - lastTeleportTime <= 20) { // 1 second (20 ticks)
                player.fallDistance = 0;
                ci.cancel();
            }
        }
    }

    private static final int NATURA_COOLDOWN = 10; // seconds
    private static final int NATURA_PROJECTILE_DAMAGE = 3;
    private static final int NATURA_ROOT_DURATION = 40; // ticks
    private static final double NATURA_MAX_RANGE = 25f; // blocks

    @Inject(method = "swing", at = @At("HEAD"))
    private void onSwingNaturaBlade(InteractionHand hand, CallbackInfo ci) {
        if ((Object) this instanceof Player player) {
            if (player.getMainHandItem().getItem() instanceof NaturaBlade) {
                if (!player.level().isClientSide) {
                    fireProjectileNatura(player);
                }
            }
        }
    }

    private void fireProjectileNatura(Player player) {
        Level level = player.level();
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            if (player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem())) return;
            Vec3 startPos = player.getEyePosition();
            Vec3 direction = player.getLookAngle().normalize();
            simulateProjectile(serverLevel, player, startPos, direction);
            player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), NATURA_COOLDOWN * 20);
        }
    }

    private void simulateProjectile(ServerLevel serverLevel, Player shooter, Vec3 startPos, Vec3 direction) {
        Vec3 currentPos = startPos;
        double stepSize = 0.5;
        double maxDistance = NATURA_MAX_RANGE;
        double traveledDistance = 0;
        while (traveledDistance < maxDistance) {
            currentPos = currentPos.add(direction.scale(stepSize));
            traveledDistance += stepSize;
            if (!(traveledDistance <= 2))
                serverLevel.sendParticles(
                        ParticleTypes.CHERRY_LEAVES,
                        currentPos.x, currentPos.y, currentPos.z,
                        2,
                        0.05, 0.05, 0,
                        0.001
                );

            Entity hitEntity = checkCollision(serverLevel, shooter, currentPos);
            if (hitEntity instanceof LivingEntity livingEntity) {
                livingEntity.hurt(shooter.damageSources().playerAttack(shooter), NATURA_PROJECTILE_DAMAGE);
                livingEntity.addEffect(new MobEffectInstance(NoezEffects.ROOT.get(), NATURA_ROOT_DURATION));
                shooter.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40,2));
                serverLevel.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                        SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 1.0F, 0.25F);
                return;
            }
        }
    }

    private Entity checkCollision(ServerLevel serverLevel, Player shooter, Vec3 position) {
        AABB box = new AABB(position, position).inflate(0.3); // 0.3 hitbox
        List<Entity> entities = serverLevel.getEntities(shooter, box);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity) {
                return entity;
            }
        }
        return null;
    }
}