package net.tiramisu.noez.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.tiramisu.noez.client.NoezTextureRenderer;

import java.util.function.Supplier;

public class SlashEffectS2CPacket {
    private final double x;
    private final double y;
    private final double z;

    public SlashEffectS2CPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void encode(SlashEffectS2CPacket packet, FriendlyByteBuf buffer) {
        buffer.writeDouble(packet.x);
        buffer.writeDouble(packet.y);
        buffer.writeDouble(packet.z);
    }

    public static SlashEffectS2CPacket decode(FriendlyByteBuf buffer) {
        return new SlashEffectS2CPacket(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static void handle(SlashEffectS2CPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            NoezTextureRenderer.renderBloodSacrificeEffect(packet.x, packet.y, packet.z);
        });
        context.get().setPacketHandled(true);
    }
}
