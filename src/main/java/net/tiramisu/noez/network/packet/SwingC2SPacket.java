package net.tiramisu.noez.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;
import net.tiramisu.noez.item.ProjectileSword;
import net.tiramisu.noez.item.SpellCaster;

import java.util.function.Supplier;

public class SwingC2SPacket {
    private static int cooldownTicks;

    public SwingC2SPacket() {
    }

    public SwingC2SPacket(int cooldownTicks){
        this.cooldownTicks = cooldownTicks;
    }

    public static void encode(SwingC2SPacket msg, FriendlyByteBuf buf) {
    }

    public static SwingC2SPacket decode(FriendlyByteBuf buf) {
        return new SwingC2SPacket();
    }

    public static void handle(SwingC2SPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                Item item = player.getMainHandItem().getItem();
                if (item instanceof ProjectileSword sword && !player.getCooldowns().isOnCooldown(item)) {
                    sword.onSwing(player, player.getMainHandItem());
                    player.getCooldowns().addCooldown(item, cooldownTicks);
                }
                if (item instanceof SpellCaster spellCaster){
                    spellCaster.onSwing(player, player.getMainHandItem());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
