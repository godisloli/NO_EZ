package net.tiramisu.noez.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.item.SpellStaff;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void StaffToolTips(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag flag, CallbackInfo ci){
        if (pStack.getItem() instanceof SpellStaff){
            tooltip.removeIf(component -> component.getString().contains("Attack Speed") || component.getString().contains("Attack Damage"));

        }
    }
}
