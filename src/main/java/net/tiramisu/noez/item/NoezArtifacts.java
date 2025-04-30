package net.tiramisu.noez.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public interface NoezArtifacts {
    void applyEffect(ServerPlayer player);
    void removeEffect(ServerPlayer player);
    void playSound(Level level, ServerPlayer serverPlayer);
}
