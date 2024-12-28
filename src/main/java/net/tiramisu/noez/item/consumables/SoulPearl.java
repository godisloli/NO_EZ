package net.tiramisu.noez.item.consumables;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.entity.NoezEntities;
import net.tiramisu.noez.entity.nonarrows.ThrownSoulPearl;
import org.jetbrains.annotations.NotNull;

public class SoulPearl extends Item {
    private static final int COOLDOWN = 12;

    public SoulPearl(Item.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack inHand = pPlayer.getItemInHand(pHand);
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
        pPlayer.getCooldowns().addCooldown(this,  COOLDOWN * 20);
        if (!pLevel.isClientSide) {
            ThrownSoulPearl thrownSoul = new ThrownSoulPearl(NoezEntities.SOUL_PEARL_ENTITY.get(), pLevel);
            thrownSoul.setItem(inHand);
            thrownSoul.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
            pLevel.addFreshEntity(thrownSoul);
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (!pPlayer.getAbilities().instabuild) {
            inHand.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(inHand, pLevel.isClientSide());
    }
}
