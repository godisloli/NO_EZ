package net.tiramisu.noez.mixin;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.tiramisu.noez.effect.NoezEffects;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    @Inject(method = "startUsingItem", at = @At("HEAD"), cancellable = true)
    private void disableInteractionWhileStunned(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        if (player.hasEffect(NoezEffects.STUN.get())) {
            ci.cancel();
        }
    }
}
