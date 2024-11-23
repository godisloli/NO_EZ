package net.tiramisu.noez.mixin;

import net.tiramisu.noez.event.LineOfSight;
import net.minecraft.world.entity.LivingEntity;  // LivingEntity is the correct class
import net.minecraft.world.entity.Entity;      // Entity is the base class for entities
import net.minecraft.world.entity.EntityType;  // EntityType is used to specify the type of entity
import net.minecraft.world.level.Level;       // World is now called Level in Minecraft 1.20.x
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)  // Mixin for the LivingEntity class
public abstract class LivingEntityMixin extends Entity {

    // Constructor for the mixin
    public LivingEntityMixin(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    // Inject into the `canSee` method to modify the behavior
    @Inject(
            at = @At("HEAD"),
            method = "hasLineOfSight",
            cancellable = true
    )
    void isLookingAtMe(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        if (entity instanceof LivingEntity) {
            if (!LineOfSight.isLookingAtYou(livingEntity, (LivingEntity) entity)) {
                cir.setReturnValue(false);
            }
        }
    }
}