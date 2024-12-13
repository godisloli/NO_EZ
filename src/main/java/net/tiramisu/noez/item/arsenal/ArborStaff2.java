package net.tiramisu.noez.item.arsenal;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.entity.arrows.RootProjectile;
import net.tiramisu.noez.item.SpellStaff;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArborStaff2 extends SpellStaff {
    private final static int cooldownTicks = 15 * 20;

    public ArborStaff2(){
        super(
                new Properties().stacksTo(1).durability(134),
                1,
                -2.4f,
                cooldownTicks,
                null
        );
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("wip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public void onSwing(Player player, ItemStack stack){
        Level level = player.level();
        if (!level.isClientSide) {
            RootProjectile projectile = new RootProjectile(level, player);
            projectile.setOwner(player);
            projectile.setLifespan(20);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F,  1F, 1.0F);
            level.addFreshEntity(projectile);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.CHERRY_LEAVES_BREAK, SoundSource.PLAYERS, 2.0F, 1.0F);
        }
    }

    @Override
    public void onActivate(Player player, ItemStack itemStack, int cooldownTicks){

    }
}
