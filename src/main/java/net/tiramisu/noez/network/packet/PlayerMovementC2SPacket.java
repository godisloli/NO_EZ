package net.tiramisu.noez.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;
import net.tiramisu.noez.effect.NoezEffects;

import java.util.*;
import java.util.function.Supplier;

public class PlayerMovementC2SPacket {
    private final Set<String> keys;

    public PlayerMovementC2SPacket(Set<String> keys) {
        this.keys = keys;
    }

    public PlayerMovementC2SPacket(FriendlyByteBuf buffer) {
        this.keys = buffer.readCollection(HashSet::new, FriendlyByteBuf::readUtf);
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeCollection(keys, FriendlyByteBuf::writeUtf);
    }

    private static final Map<UUID, Long> lastDamageTime = new HashMap<>();
    private static final int TICK_INTERVAL = 5; // 5 ticks (0.25 seconds)

    public static void handle(PlayerMovementC2SPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            if (player.hasEffect(NoezEffects.ROOT.get())) {
                int amplifier = player.getEffect(NoezEffects.ROOT.get()).getAmplifier();
                if (packet.keys.contains("W") || packet.keys.contains("A") || packet.keys.contains("S") || packet.keys.contains("D") || packet.keys.contains("SPACE")) {
                    UUID playerId = player.getUUID();
                    long currentTime = player.level().getGameTime();
                    if (!lastDamageTime.containsKey(playerId) || (currentTime - lastDamageTime.get(playerId)) >= TICK_INTERVAL) {
                        player.hurt(player.damageSources().sweetBerryBush(), 0.5f * amplifier);
                        lastDamageTime.put(playerId, currentTime);
                    }
                    player.setDeltaMovement(0.0, Math.min(0.0, player.getDeltaMovement().y), 0.0);
                    player.hurtMarked = true;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
