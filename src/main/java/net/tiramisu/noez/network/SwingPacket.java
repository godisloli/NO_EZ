package net.tiramisu.noez.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;
import net.tiramisu.noez.item.ProjectileSword;

import java.util.function.Supplier;

public class SwingPacket {
    private static int cooldownTicks;

    public SwingPacket() {
    }

    public SwingPacket(int cooldownTicks){
        this.cooldownTicks = cooldownTicks;
    }

    public static void encode(SwingPacket msg, FriendlyByteBuf buf) {
    }

    public static SwingPacket decode(FriendlyByteBuf buf) {
        return new SwingPacket();
    }

    public static void handle(SwingPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                Item item = player.getMainHandItem().getItem();
                if (item instanceof ProjectileSword sword && !player.getCooldowns().isOnCooldown(item)) {
                    sword.onSwing(player, player.getMainHandItem());
                    player.getCooldowns().addCooldown(item, cooldownTicks);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
