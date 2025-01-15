package net.tiramisu.noez.item.armors;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EndWalkerElytra extends ElytraItem {
    public EndWalkerElytra(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("end_walker_elytra_tooltip"));
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (level.dimension().equals(Level.END) && player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof EndWalkerElytra) {
            if (player.isFallFlying()) {
                var lookDirection = player.getLookAngle();
                double boostMultiplier = 0.005;
                player.push(lookDirection.x * boostMultiplier,
                        lookDirection.y * boostMultiplier,
                        lookDirection.z * boostMultiplier);
                spawnParticles(player, level);
            }
        }
    }

    private void spawnParticles(Player player, Level level) {
        if (level instanceof ServerLevel serverLevel)
            serverLevel.sendParticles(
                ParticleTypes.REVERSE_PORTAL,
                player.getX(),
                player.getY(),
                player.getZ(),
                3,
                0.5, 0.5, 0.5,
                0.1
        );
    }
}
