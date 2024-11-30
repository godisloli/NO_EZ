package net.tiramisu.noez.mixin;

import net.minecraft.world.inventory.EnchantmentMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentMenu.class)
public class EnchantmentMenuMixin {
    @Inject(method = "slotsChanged", at = @At("HEAD"), cancellable = true)
    private void disableEnchanting(CallbackInfo ci) {
        ci.cancel();
    }
}
