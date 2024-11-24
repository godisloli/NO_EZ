package net.tiramisu.noez.mixin;

import net.minecraft.client.Minecraft;
import net.tiramisu.noez.effect.NoezEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void disableAttackWhileStunned(CallbackInfoReturnable<Boolean> ci) {
        Minecraft mc = (Minecraft) (Object) this;
        if (mc.player != null && mc.player.hasEffect(NoezEffects.STUN.get())) {
            ci.cancel();
        }
    }
    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void disableAttackWhileStunned(CallbackInfo ci) {
        Minecraft mc = (Minecraft) (Object) this;
        if (mc.player != null && mc.player.hasEffect(NoezEffects.STUN.get())) {
            ci.cancel();
        }
    }
}