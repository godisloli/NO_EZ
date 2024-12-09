package net.tiramisu.noez.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.event.global.LineOfSight;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.item.weaponstools.NaturaBlade;
import net.tiramisu.noez.util.NoezTags;
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
                if (entity.getType().is(NoezTags.Mobs.NO_LINE_OF_SIGHT))
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

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void reduceDamageOnLowDurability(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity target = (LivingEntity) (Object) this;
        if (pSource.getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();
            if (weapon.getDamageValue() >= weapon.getMaxDamage() - 1 && weapon.getMaxDamage() > 1) {
                target.hurt(target.damageSources().generic(), 1.0F);
                if (attacker instanceof Player){
                    attacker.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(),
                            SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                cir.cancel();
            }
        }
    }
}