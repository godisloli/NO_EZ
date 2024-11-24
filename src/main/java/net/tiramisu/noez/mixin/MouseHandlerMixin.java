package net.tiramisu.noez.mixin;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.Minecraft;
import net.tiramisu.noez.effect.NoezEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Inject(method = "onMove", at = @At("HEAD"), cancellable = true)
    private void disableMouseMovementWhileStunned(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player != null && player.hasEffect(NoezEffects.STUN.get())) {
            ci.cancel();
        }
    }

    @Inject(method = "onPress",at = @At("HEAD"), cancellable = true)
    private void disableClickWhileStunned(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player != null && player.hasEffect(NoezEffects.STUN.get())) {
            ci.cancel();
        }
    }
}