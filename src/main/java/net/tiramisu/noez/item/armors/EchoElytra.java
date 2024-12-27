package net.tiramisu.noez.item.armors;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.item.NoezItems;
import net.tiramisu.noez.sound.NoezSounds;
import org.jetbrains.annotations.NotNull;

public class EchoElytra extends ElytraItem {
    private static final int COOLDOWN = 8;

    public EchoElytra(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack pToRepair, ItemStack pRepair) {
        return pRepair.is(NoezItems.WARDEN_HEART.get());
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (player.isFallFlying() && player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof EchoElytra) {
            spawnParticleTrail(level, player);
            if (!player.getCooldowns().isOnCooldown(this)) {
                var lookDirection = player.getLookAngle();
                double boostMultiplier = 4.0;
                player.push(lookDirection.x * boostMultiplier,
                        lookDirection.y * boostMultiplier,
                        lookDirection.z * boostMultiplier);
                spawnBoostParticle(player, level);
            }
            player.getCooldowns().addCooldown(this, COOLDOWN * 20);
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        return super.use(pLevel, pPlayer, pHand);
    }

    private void spawnParticleTrail(Level level, Player player) {
        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 3; i++) {
                double offsetX = (level.random.nextDouble() - 0.5) * 0.2;
                double offsetY = (level.random.nextDouble() - 0.5) * 0.2;
                double offsetZ = (level.random.nextDouble() - 0.5) * 0.2;
                serverLevel.sendParticles(ParticleTypes.SCULK_SOUL,
                        player.getX() + offsetX,
                        player.getY() + offsetY + 0.5,
                        player.getZ() + offsetZ,
                        1, 0, 0, 0, 0.1);
            }
        }
    }


    private void spawnBoostParticle(Player player, Level level) {
        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 50; i++) {
                double offsetX = (level.random.nextDouble() - 0.5) * 2.0;
                double offsetY = (level.random.nextDouble() - 0.5) * 2.0;
                double offsetZ = (level.random.nextDouble() - 0.5) * 2.0;

                double centerX = player.getX();
                double centerY = player.getY() + 0.5;
                double centerZ = player.getZ();

                serverLevel.sendParticles(
                        ParticleTypes.SCULK_CHARGE_POP,
                        centerX,
                        centerY,
                        centerZ,
                        1,
                        offsetX,
                        offsetY,
                        offsetZ,
                        0.2
                );
            }
            serverLevel.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    NoezSounds.ECHO_ELYTRA_UPDRAFT.get(),
                    SoundSource.PLAYERS,
                    2f,
                    1f
                    );
        }
    }
}

