package net.tiramisu.noez.mixin;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.tiramisu.noez.effect.NoezEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Inject(method = "startRiding", at = @At("HEAD"), cancellable = true)
    private void disableServerItemUse(CallbackInfoReturnable<InteractionResult> cir) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        if (player.hasEffect(NoezEffects.STUN.get())) {
            cir.setReturnValue(InteractionResult.FAIL); // Cancel the action server-side
        }
    }
}
