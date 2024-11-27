package net.tiramisu.noez.mixin;

import net.minecraft.world.entity.Mob;
import net.tiramisu.noez.effect.NoezEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobMixin {
    @Inject(method = "serverAiStep", at = @At("HEAD"), cancellable = true)
    private void disablePathfindingWhileStunned(CallbackInfo ci) {
        Mob mob = (Mob) (Object) this;
        if (mob.hasEffect(NoezEffects.STUN.get())) {
            ci.cancel();
        }
    }
}
