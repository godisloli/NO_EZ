package net.tiramisu.noez.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tiramisu.noez.network.packet.ArtifactSyncS2CPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ArtifactManager {
    private static final Map<UUID, ItemStack> activeArtifacts = new HashMap<>();

    public static void setActiveArtifact(ServerPlayer player, ItemStack artifact) {
        UUID playerId = player.getUUID();
        removeCurrentArtifact(player);

        if (artifact.getItem() instanceof NoezArtifacts artifactItem) {
            activeArtifacts.put(playerId, artifact.copy());
            artifactItem.applyEffect(player);
            ArtifactSyncS2CPacket.send(player, artifact);
        }
    }

    public static void removeCurrentArtifact(ServerPlayer player) {
        UUID playerId = player.getUUID();
        ItemStack previous = activeArtifacts.remove(playerId);
        if (previous != null && previous.getItem() instanceof NoezArtifacts artifactItem) {
            artifactItem.removeEffect(player);
        }
    }

    public static Optional<ItemStack> getActiveArtifact(Player player) {
        return Optional.ofNullable(activeArtifacts.get(player.getUUID()));
    }
}
