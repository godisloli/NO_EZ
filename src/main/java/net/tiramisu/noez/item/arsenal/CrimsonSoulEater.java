package net.tiramisu.noez.item.arsenal;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.entity.arrows.RootProjectile;
import net.tiramisu.noez.entity.nonarrows.CrimsonScytheShot;
import net.tiramisu.noez.item.Critable;
import net.tiramisu.noez.item.LifeStealable;
import net.tiramisu.noez.item.NoezToolTier;
import net.tiramisu.noez.item.ProjectileSword;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrimsonSoulEater extends ProjectileSword implements Critable, LifeStealable {
    private static final double CRIT_CHANCE = 0.15;
    private static final double CRIT_DAMAGE = 1.5;
    private static final double LIFESTEAL = 0.12;
    private static final int COOLDOWN = 5 * 20;
    private boolean ALWAYS_CRIT = false;

    public CrimsonSoulEater() {
        super(
                new Properties().stacksTo(1).durability(190),
                COOLDOWN,
                null,
                NoezToolTier.MEDIUM,
                5,
                -2.4f
        );
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.getItem() instanceof CrimsonSoulEater;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("noez.crimson_soul_eater.tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.crimson_soul_eater.tooltip2"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public void onSwing(Player player, ItemStack stack) {
        Level level = player.level();
        if (!level.isClientSide) {
//            CrimsonScytheShot projectile = new CrimsonScytheShot(player, level);
//            projectile.setOwner(player);
//            projectile.setLifespan(40);
//            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F,  2.0F, 1.0F);
//            level.addFreshEntity(projectile);
//            level.playSound(null, player.getX(), player.getY(), player.getZ(),
//                    SoundEvents.CHERRY_LEAVES_BREAK, SoundSource.PLAYERS, 2.0F, 1.0F);
        }
    }

    @Override
    public double getCritChance() {
        return CRIT_CHANCE;
    }

    @Override
    public double getCritDamageAmplifier() {
        return CRIT_DAMAGE;
    }

    @Override
    public boolean isAlwaysCrit(){
        return ALWAYS_CRIT;
    }

    @Override
    public void setAlwaysCrit(boolean value){
        this.ALWAYS_CRIT = value;
    }

    @Override
    public double getLifeStealAmount() {
        return LIFESTEAL;
    }
}
