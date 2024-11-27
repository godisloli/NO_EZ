package net.tiramisu.noez.item.weaponstools;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class NaturaBlade extends SwordItem {
    private static final int Cooldown = 5;

    public NaturaBlade(Tier tier, int attackDamage, float attackSpeed, Item.Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("noez.natura_blade.tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.natura_blade.tooltip2"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
//    private void spawnParticles(Player player) {
//        if (!player.level().isClientSide && player.level() instanceof ServerLevel serverLevel) {// Ensure this runs only on the client
//            Level level = player.level(); // Get the player's level (world)
//            double radius = 2; // Radius of the particle circle
//            int particleCount = 25; // Number of particles in the circle
//
//            for (int i = 0; i < particleCount; i++) {
//                double angle = 2 * Math.PI * i / particleCount; // Calculate angle for each particle
//                double xOffset = radius * Math.cos(angle); // X offset
//                double zOffset = radius * Math.sin(angle); // Z offset
//
//                serverLevel.sendParticles(
//                        ParticleTypes.HAPPY_VILLAGER, // Type of particle
//                        player.getX() + xOffset,     // X position
//                        player.getY() + 0.5,           // Y position (slightly above the player)
//                        player.getZ() + zOffset,     // Z position
//                        1,                           // Number of particles (1 per position)
//                        0, 0, 0,     // No randomness in spread
//                        0.01);
//            }
//        }
//    } //Ctrl K + C
}