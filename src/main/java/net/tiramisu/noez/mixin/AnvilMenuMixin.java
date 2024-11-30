package net.tiramisu.noez.mixin;

import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void disableAnvilEnchantments(CallbackInfo ci) {
        // Cancel the anvil result creation
        ci.cancel();
    }
}
