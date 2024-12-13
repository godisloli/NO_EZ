package net.tiramisu.noez.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.attribute.ManaPlayer;
import net.tiramisu.noez.network.NoezNetwork;
import net.tiramisu.noez.network.packet.ManaDataSyncS2CPacket;

@Mod.EventBusSubscriber
public class CapabilityAttachHandler {
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            ManaPlayer manaProvider = new ManaPlayer();
            manaProvider.setOwner(player); // Assign the owning player
            event.addCapability(new ResourceLocation(NOEZ.MOD_ID, "properties"), manaProvider);
        }
    }


    @SubscribeEvent
    public static void onRespawn(PlayerEvent.Clone event){
        if(event.isWasDeath()){
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(ManaPlayer.MANA).ifPresent(oldStore -> {
                event.getEntity().getCapability(ManaPlayer.MANA).ifPresent(newStore -> {
                    newStore.setMana(newStore.getMaxMana());
                });
            });
            event.getOriginal().invalidateCaps();
        }
    }

    @SubscribeEvent
    public static void Tick(LivingEvent.LivingTickEvent event){
        if (event.getEntity() instanceof ServerPlayer serverPlayer){
            serverPlayer.getCapability(ManaPlayer.MANA).ifPresent(mana -> {
                NoezNetwork.sendDataToClient(serverPlayer, new ManaDataSyncS2CPacket(mana.getMana()));
            });
        }
    }
}
