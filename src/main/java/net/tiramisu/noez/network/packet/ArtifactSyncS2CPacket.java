package net.tiramisu.noez.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.tiramisu.noez.client.ArtifactData;
import net.tiramisu.noez.network.NoezNetwork;

import java.util.function.Supplier;

public class ArtifactSyncS2CPacket {
    private final ItemStack artifact;

    public ArtifactSyncS2CPacket(ItemStack artifact) {
        this.artifact = artifact;
    }

    public ArtifactSyncS2CPacket(FriendlyByteBuf buf) {
        this.artifact = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(artifact);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ArtifactData.handleSync(artifact);
        });
        context.setPacketHandled(true);
    }

    public static void send(ServerPlayer player, ItemStack stack) {
        NoezNetwork.sendDataToClient(player, new ArtifactSyncS2CPacket(stack));
    }
}
