package net.tiramisu.noez.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void preventBreaking(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            stack.setDamageValue(stack.getMaxDamage() - 1);
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void preventActionBrokenTool(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getDamageValue() == stack.getMaxDamage() - 1) {
            cir.setReturnValue(InteractionResultHolder.fail(stack));
        }
    }
}

