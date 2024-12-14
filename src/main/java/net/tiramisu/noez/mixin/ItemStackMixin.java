package net.tiramisu.noez.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.tiramisu.noez.item.Critable;
import net.tiramisu.noez.item.SpellStaff;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;


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

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void preventUseBrokenTool(CallbackInfoReturnable<InteractionResult> cir){
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getDamageValue() == stack.getMaxDamage() - 1) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }

    @Inject(method = "getTooltipLines", at = @At("RETURN"), cancellable = true)
    private void staffTooltip(@Nullable Player pPlayer, TooltipFlag pIsAdvanced, CallbackInfoReturnable<List<Component>> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof SpellStaff spellStaff) {
            List<Component> tooltip = cir.getReturnValue();
            double attackSpeed = 4 + spellStaff.getAttackSpeed();
            double attackDamage = spellStaff.getAttackDamage();
            for (int i = 0; i < tooltip.size(); i++) {
                Component line = tooltip.get(i);
                if (line.getString().contains("Attack Damage")) {
                    MutableComponent attackDamageComponent = Component.literal(String.format(" %.1f Attack Damage", attackDamage))
                            .withStyle(style -> style.withColor(ChatFormatting.DARK_GREEN));
                    tooltip.set(i, attackDamageComponent);
                    break;
                }
            }
            for (int i = 0; i < tooltip.size(); i++) {
                Component line = tooltip.get(i);
                if (line.getString().contains("Attack Speed")) {
                    MutableComponent attackSpeedComponent = Component.literal(String.format(" %.1f Attack Speed", attackSpeed))
                            .withStyle(style -> style.withColor(ChatFormatting.DARK_GREEN));
                    tooltip.set(i, attackSpeedComponent);
                    break;
                }
            }
            cir.setReturnValue(tooltip);
        }
    }

    @Inject(method = "getTooltipLines", at = @At("RETURN"), cancellable = true)
    private void critableTooltip(@Nullable Player pPlayer, TooltipFlag pIsAdvanced, CallbackInfoReturnable<List<Component>> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof Critable critable) {
            List<Component> tooltip = cir.getReturnValue();
            double critChance = critable.getCritChance() * 100;
            double critDamageAmplifier = critable.getCritDamageAmplifier() * 100;
            for (int i = 0; i < tooltip.size(); i++) {
                Component line = tooltip.get(i);
                if (line.getString().contains("When in Main Hand:")) {
                    tooltip.add(i + 3, Component.translatable("tooltip.noez.crit_chance", String.format("%.0f%%", critChance))
                            .withStyle(style -> style.withColor(ChatFormatting.RED)));
                    tooltip.add(i + 4, Component.translatable("tooltip.noez.crit_damage", String.format("%.0f%%", critDamageAmplifier))
                            .withStyle(style -> style.withColor(ChatFormatting.RED)));
                    break;
                }
            }
            cir.setReturnValue(tooltip);
        }
    }

}

