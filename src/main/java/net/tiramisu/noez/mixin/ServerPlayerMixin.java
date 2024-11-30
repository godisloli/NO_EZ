package net.tiramisu.noez.mixin;
import net.minecraft.server.level.ServerPlayer;
import net.tiramisu.noez.event.global.InvisibleRework;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Inject(method = "swing", at = @At("HEAD"), cancellable = true)
    private void InvisibleOnSwing(CallbackInfo ci){
        if ((Object) this instanceof ServerPlayer player){
            InvisibleRework.handleInvisibility(player);
        }
    }
}
