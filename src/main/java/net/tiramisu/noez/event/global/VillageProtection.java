package net.tiramisu.noez.event.global;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.attribute.NoezAttributes;
import net.tiramisu.noez.util.VillageDetect;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber
public class VillageProtection {
    private static final Set<BlockPos> playerPlacedBlocks = new HashSet<>();

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        removeBlock(event.getPlayer().level(), event.getPos());
    }

    @SubscribeEvent
    public static void ironGolemAggressive(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity instanceof IronGolem ironGolem && !ironGolem.isDeadOrDying()) {
            Level level = ironGolem.level();

            Player targetPlayer = null;
            for (Player player : level.getEntitiesOfClass(Player.class, ironGolem.getBoundingBox().inflate(10.0))) {
                if (shouldMobAttack(player)) {
                    targetPlayer = player;
                    break;
                }
            }

            if (targetPlayer != null) {
                ironGolem.setTarget(targetPlayer);
            }
        }
    }

    private static boolean shouldMobAttack(Player player) {
        if (player.getAttributes().hasAttribute(NoezAttributes.REPUTATION.get()) && !player.getAbilities().instabuild) {
            double reputation = player.getAttributeValue(NoezAttributes.REPUTATION.get());
            return reputation < -200;
        }
        return false;
    }

    @SubscribeEvent
    public static void killVillager(LivingDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        Entity killer = event.getSource().getEntity();
        if (livingEntity instanceof Villager && killer instanceof Player player) {
            decreaseReputation(player, 100);
            alertIronGolems(player.level(), player.blockPosition(), player);
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        Level level = event.getEntity().level();
        BlockPos pos = event.getPos();

        if (event.getEntity() instanceof Player) {
            markPlayerPlacedBlock(level, pos);
        }
    }

    public static void markPlayerPlacedBlock(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            playerPlacedBlocks.add(pos.immutable());
        }
    }

    public static void removeBlock(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            playerPlacedBlocks.remove(pos.immutable());
        }
    }

    public static boolean isPlayerPlaced(Level level, BlockPos pos) {
        return playerPlacedBlocks.contains(pos.immutable());
    }

    @SubscribeEvent
    public static void playerGrief(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Level level = player.level();
        BlockPos pos = event.getPos();
        if (isPlayerPlaced(level, pos)) {
            return;
        }

        if (VillageDetect.isInVillage(level, pos) && canVillagersSee(player, level, pos)) {
            decreaseReputation(player, 5);
        }
    }

    @SubscribeEvent
    public static void onChestOpened(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        BlockState blockState = level.getBlockState(pos);

        if (!(blockState.getBlock() instanceof ChestBlock) || player.getAbilities().instabuild) {
            return;
        }

        if (!VillageDetect.isInVillage(level, pos)) {
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ChestBlockEntity chestEntity) {
            CompoundTag tag = chestEntity.saveWithFullMetadata();
            if (!tag.contains("LootTable")) {
                return;
            }
        }

        if (VillageDetect.isInVillage(level, pos) && canVillagersSee(player, level, pos)) {
            alertIronGolems(level, pos, player);
            decreaseReputation(player, 20);
        }
    }

    private static boolean canVillagersSee(Player player, Level level, BlockPos chestPos) {
        List<Villager> villagers = level.getEntitiesOfClass(Villager.class,
                new AABB(chestPos).inflate(10));

        for (Villager villager : villagers) {
            if (LineOfSight.isLookingAtYou(villager, player) && LineOfSight.canSeeThroughBlocks(villager, player)) {
                level.playSound(
                        null,
                        villager.getX(),
                        villager.getY(),
                        villager.getZ(),
                        SoundEvents.VILLAGER_NO,
                        SoundSource.NEUTRAL,
                        1f,
                        1f
                );
                return true;
            }
        }
        return false;
    }

    private static void alertIronGolems(Level level, BlockPos alertPos, Player player) {
        List<IronGolem> golems = level.getEntitiesOfClass(IronGolem.class,
                new AABB(alertPos).inflate(100));
        for (IronGolem golem : golems) {
            golem.setTarget(player);
        }
    }

    private static void decreaseReputation(Player player, int amount) {
        if (player.getAttributes().hasAttribute(NoezAttributes.REPUTATION.get())) {
            double reputation = player.getAttributeValue(NoezAttributes.REPUTATION.get());
            player.getAttribute(NoezAttributes.REPUTATION.get()).setBaseValue(reputation - amount);
            System.out.println("Player's Reputation is: " + player.getAttribute(NoezAttributes.REPUTATION.get()).getValue());
        }
    }
}
