package net.tiramisu.noez.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SwordItem.class, remap = false)
public class PlayerMixin{
    @Inject(method = "canPerformAction", at = @At("HEAD"), cancellable = true)
    private void noSweepingAttack(ItemStack itemStack, ToolAction toolAction, CallbackInfoReturnable<Boolean> cir){
        if (toolAction == ToolActions.SWORD_SWEEP){
            cir.setReturnValue(false);
        }
    }
}
