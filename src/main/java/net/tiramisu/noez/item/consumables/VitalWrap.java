package net.tiramisu.noez.item.consumables;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.effect.NoezEffects;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VitalWrap extends Item {
    private static final int HEAL_AMOUNT = 3;
    private static final int COOLDOWN = 5;

    public VitalWrap(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("noez.vital_wrap.tooltip"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (player.getCooldowns().isOnCooldown(this) || player.getAbilities().instabuild) {
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        ItemStack itemStack = player.getItemInHand(hand);
        if (!player.getCooldowns().isOnCooldown(this)) {
            if (player.hasEffect(NoezEffects.WOUND.get()))
                player.removeEffect(NoezEffects.WOUND.get());
            if (player.hasEffect(NoezEffects.BLEED.get()))
                player.removeEffect(NoezEffects.BLEED.get());
            player.heal(HEAL_AMOUNT);
            itemStack.shrink(1);
            player.getCooldowns().addCooldown(this, COOLDOWN * 20);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
    }
}
