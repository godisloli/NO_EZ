package net.tiramisu.noez.attribute;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tiramisu.noez.NOEZ;

public class CapabilityAttachHandler {
    private static final ResourceLocation MANA_CAP = new ResourceLocation(NOEZ.MOD_ID, "mana");

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(MANA_CAP, new AttributePlayer());
        }
    }
}
