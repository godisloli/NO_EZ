package net.tiramisu.noez.mixin;

import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.event.LineOfSight;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable =  true)
    private void DisableMovementWhileStunned(CallbackInfo ci){
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        if (livingEntity.hasEffect(NoezEffects.STUN.get())){
            ci.cancel();
        }
    }
}