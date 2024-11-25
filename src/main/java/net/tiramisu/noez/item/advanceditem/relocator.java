package net.tiramisu.noez.item.advanceditem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class relocator extends Item {

    private static final int TELEPORT_DISTANCE = 4; // Max teleport distance
    private static final int COOLDOWN_SECONDS = 10; // Cooldown duration in seconds

    public relocator(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }

        if (!world.isClientSide && player instanceof ServerPlayer serverPlayer) {
            ItemStack stack = player.getItemInHand(hand);

            // Get player's current position and look direction
            Vec3 startPos = player.getEyePosition(); // Eye position
            Vec3 lookVec = player.getLookAngle(); // Direction the player is looking
            Vec3 endPos = startPos.add(lookVec.scale(TELEPORT_DISTANCE)); // End position

            // Perform ray tracing to find the nearest collision
            HitResult hitResult = world.clip(new ClipContext(
                    startPos,
                    endPos,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player
            ));

            // Calculate the final teleportation position
            Vec3 teleportPos = (hitResult.getType() == HitResult.Type.BLOCK)
                    ? hitResult.getLocation().subtract(lookVec.scale(0.5)) // Teleport just before the block
                    : endPos; // Max range if no block is hit

            BlockPos blockPos = new BlockPos((int)teleportPos.x(), (int)teleportPos.y(), (int)teleportPos.z());

            if (world.isEmptyBlock(blockPos) && world.isEmptyBlock(blockPos.above())) {
                serverPlayer.teleportTo(teleportPos.x(), teleportPos.y(), teleportPos.z());
                createTeleportEffects(world, teleportPos);
                // Add cooldown to the item
                player.getCooldowns().addCooldown(this, COOLDOWN_SECONDS * 20); // 20 ticks per second
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());

    }
    private void createTeleportEffects(Level world, Vec3 position) {
        if (world instanceof ServerLevel serverWorld) {
            // Play the Enderman teleport sound
            serverWorld.playSound(
                    null, // Null source means all players will hear it
                    new BlockPos((int)position.x(), (int)position.y(), (int)position.y()),
                    SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS,
                    1.0f, // Volume
                    1.0f  // Pitch
            );

            // Spawn particles around the position
            for (int i = 0; i < 32; i++) {
                double offsetX = (world.random.nextDouble() - 0.5) * 2.0;
                double offsetY = world.random.nextDouble() - 0.5;
                double offsetZ = (world.random.nextDouble() - 0.5) * 2.0;

                serverWorld.sendParticles(
                        ParticleTypes.PORTAL, // Portal particle type
                        position.x, position.y, position.z, // Position
                        15, // Count
                        offsetX, offsetY, offsetZ, // Offset for particle spread
                        0.6 // Speed
                );
            }
        }
    }
}
