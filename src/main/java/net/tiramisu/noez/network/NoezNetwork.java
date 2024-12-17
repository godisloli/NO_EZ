package net.tiramisu.noez.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.tiramisu.noez.network.packet.ManaDataSyncS2CPacket;
import net.tiramisu.noez.network.packet.SlashEffectS2CPacket;
import net.tiramisu.noez.network.packet.SwingC2SPacket;
import org.lwjgl.system.windows.MSG;

public class NoezNetwork {
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("noez", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        int packetId = 0;
        CHANNEL.registerMessage(
                packetId++,
                SwingC2SPacket.class,
                SwingC2SPacket::encode,
                SwingC2SPacket::decode,
                SwingC2SPacket::handle
        );
        CHANNEL.registerMessage(
                packetId++,
                ManaDataSyncS2CPacket.class,
                ManaDataSyncS2CPacket::toBytes,
                ManaDataSyncS2CPacket::new, // Constructor for decoding
                ManaDataSyncS2CPacket::handle
        );
        CHANNEL.registerMessage(
                packetId++,
                SlashEffectS2CPacket.class,
                SlashEffectS2CPacket::encode,
                SlashEffectS2CPacket::decode,
                SlashEffectS2CPacket::handle
        );
    }

    public static <MSG>  void sendDataToClient(ServerPlayer player, MSG message) {
        NoezNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                message
        );
    }
}
