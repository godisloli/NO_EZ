package net.tiramisu.noez.item.artifacts;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.item.ArtifactItem;

public class PhoenixFeather extends ArtifactItem {
    public PhoenixFeather(Properties properties) {
        super(properties);
    }

    @Override
    public void applyEffect(ServerPlayer player) {
        //WIP
    }

    @Override
    public void removeEffect(ServerPlayer player) {
        //WIP
    }

    @Override
    public void playSound(Level level, ServerPlayer player) {
        level.playSound(null, player.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1f, 1f);
    }
}