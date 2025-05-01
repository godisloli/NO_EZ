package net.tiramisu.noez.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class ArtifactManager {
    private static final Map<UUID, ItemStack> activeArtifacts = new HashMap<>();

    public static void setActiveArtifact(ServerPlayer player, ItemStack artifact) {
        activeArtifacts.put(player.getUUID(), artifact.copy());
        if (artifact.getItem() instanceof ArtifactItem ai) {
            ai.playSound(player.level(), player);
        }
    }

    public static void removeArtifact(ServerPlayer player) {
        UUID uuid = player.getUUID();
        ItemStack old = activeArtifacts.remove(uuid);
        if (old != null && old.getItem() instanceof ArtifactItem ai) {
            ai.removeEffect(player);
        }
    }

    public static void tickArtifacts(ServerLevel level) {
        for (ServerPlayer player : level.getPlayers(p -> true)) {
            ItemStack artifact = activeArtifacts.get(player.getUUID());
            if (artifact != null && artifact.getItem() instanceof ArtifactItem ai) {
                ai.applyEffect(player);
            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.LevelTickEvent event) {
        if (!event.level.isClientSide && event.phase == TickEvent.Phase.END && event.level instanceof ServerLevel serverLevel) {
            ArtifactManager.tickArtifacts(serverLevel);
        }
    }
}

