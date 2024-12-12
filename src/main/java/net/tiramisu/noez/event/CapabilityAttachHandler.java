package net.tiramisu.noez.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.attribute.ManaPlayer;

@Mod.EventBusSubscriber
public class CapabilityAttachHandler {
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(NOEZ.MOD_ID, "properties"),new ManaPlayer());
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.Clone event){
        if(event.isWasDeath()){
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(ManaPlayer.MANA).ifPresent(oldStore -> {
                event.getEntity().getCapability(ManaPlayer.MANA).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().invalidateCaps();
        }
    }
}
