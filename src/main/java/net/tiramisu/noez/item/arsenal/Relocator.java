package net.tiramisu.noez.item.arsenal;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.tiramisu.noez.particles.NoezParticles;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class Relocator extends Item {

    private static final int TELEPORT_DISTANCE = 10;
    private static final int COOLDOWN_SECONDS = 8;

    public Relocator(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("noez.relocator.tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.relocator.tooltip2"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.getItem() instanceof Relocator)
            itemStack.hurt(1, RandomSource.create(), null);
        if (!world.isClientSide && player instanceof ServerPlayer serverPlayer) {
            Vec3 startPos = player.getEyePosition();
            Vec3 lookVec = player.getLookAngle();
            Vec3 endPos = startPos.add(lookVec.scale(TELEPORT_DISTANCE));

            HitResult hitResult = world.clip(new ClipContext(
                    startPos,
                    endPos,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player
            ));

            Vec3 teleportPos = (hitResult.getType() == HitResult.Type.BLOCK)
                    ? hitResult.getLocation().subtract(lookVec.scale(0.5))
                    : endPos;

            BlockPos blockPos = new BlockPos((int)teleportPos.x(), (int)teleportPos.y(), (int)teleportPos.z());

            if (world.isEmptyBlock(blockPos) && world.isEmptyBlock(blockPos.above())) {
                createTeleportEffects(world, teleportPos, serverPlayer);
                serverPlayer.teleportTo(teleportPos.x(), teleportPos.y(), teleportPos.z());
                player.getCooldowns().addCooldown(this, COOLDOWN_SECONDS * 20);
                player.getPersistentData().putLong("RelocatorTeleportTime", world.getGameTime());
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
    }

    private void createTeleportEffects(Level world, Vec3 position, Player player) {
        if (world instanceof ServerLevel serverWorld) {
            serverWorld.playSound(
                    null,
                    new BlockPos((int) position.x(), (int) position.y(), (int) position.z()),
                    SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS,
                    1.0f,
                    1.0f
            );

            Random random = new Random();
            int particleCount = 100;
            double radius = 1;
            for (int i = 0; i < particleCount; i++) {
                double xOffset = (random.nextDouble() * 2 - 1) * radius;
                double yOffset = 0.0f;
                double zOffset = (random.nextDouble() * 2 - 1) * radius;

                double xVel = random.nextDouble() * 1.0 + 0.2;
                double zVel = random.nextDouble() * 1.0 + 0.2;
                double yVel = random.nextDouble() * 0.5 + 0.5;
                if (random.nextBoolean()) {
                    yVel = -yVel;
                }
                Vec3 particlePos = new Vec3(player.getX() + xOffset, player.getY() + yOffset + 1.0, player.getZ() + zOffset);
                serverWorld.sendParticles(
                        NoezParticles.BUTTERFLY.get(),
                        particlePos.x(), particlePos.y(), particlePos.z(),
                        1,
                        xVel, yVel, zVel,
                        0.1
                );
            }
        }
    }
}
