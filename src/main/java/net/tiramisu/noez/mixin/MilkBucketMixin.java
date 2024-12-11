package net.tiramisu.noez.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
public class MilkBucketMixin {
    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    private void noCleanseMilkBucket(ItemStack stack, Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir){
        if (livingEntity instanceof Player player) {
            if (!level.isClientSide && player != null) {
                ItemStack emptyBucket = new ItemStack(Items.BUCKET);
                cir.setReturnValue(emptyBucket);
            }
        }
    }
}
