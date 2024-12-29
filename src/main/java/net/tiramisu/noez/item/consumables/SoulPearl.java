package net.tiramisu.noez.item.consumables;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.entity.nonarrows.ThrownSoulPearl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulPearl extends Item {
    private static final int COOLDOWN = 12;

    public SoulPearl(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("noez.soul_pearl.tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.soul_pearl.tooltip2", COOLDOWN));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack inHand = pPlayer.getItemInHand(pHand);
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
        pPlayer.getCooldowns().addCooldown(this,  COOLDOWN * 20);
        if (pLevel instanceof ServerLevel serverLevel) {
            ThrownSoulPearl thrownSoul = new ThrownSoulPearl(pLevel, pPlayer);
            thrownSoul.setItem(inHand);
            thrownSoul.setPos(pPlayer.getX(), pPlayer.getEyeY() - 0.1F, pPlayer.getZ());
            thrownSoul.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
            serverLevel.addFreshEntity(thrownSoul);
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (!pPlayer.getAbilities().instabuild) {
            inHand.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(inHand, pLevel.isClientSide());
    }
}
