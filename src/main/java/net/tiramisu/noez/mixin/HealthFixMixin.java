package net.tiramisu.noez.mixin;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.tiramisu.noez.util.HealthFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LivingEntity.class})
public abstract class HealthFixMixin implements HealthFix {
    @Unique
    @Nullable
    private Float actualHealth = null;

    public HealthFixMixin() {
    }

    @Inject(method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("HEAD"))
    private void maxhealthfix$readAdditionalSaveData(CompoundTag tag, CallbackInfo callback) {
        if (tag.contains("Health")) {
            float savedHealth = tag.getFloat("Health");
            if (savedHealth > this.getHealth() && savedHealth > 0.0F) {
                this.actualHealth = savedHealth;
            }
        }
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void maxhealthfix$tick(CallbackInfo callback) {
        if (this.actualHealth != null) {
            if (this.actualHealth > 0.0F && this.actualHealth > this.getMaxHealth()) {
                this.setHealth(this.actualHealth);
            }

            this.actualHealth = null;
        }
    }

    public void maxhealthfix$setRestorePoint(float restorePoint) {
        this.actualHealth = restorePoint;
    }

    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract float getMaxHealth();

    @Shadow
    public abstract void setHealth(float health);
}
