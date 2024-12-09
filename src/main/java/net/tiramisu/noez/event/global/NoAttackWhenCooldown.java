package net.tiramisu.noez.event.global;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class NoAttackWhenCooldown {

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (event.getEntity() instanceof LocalPlayer player) {
            if (player.getAttackStrengthScale(0.0F) < 1.0F) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onClickEvent(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player != null && event.isAttack()) {
            if (player.getAttackStrengthScale(0.0F) < 1.0F) {
                event.setSwingHand(false);
                event.setCanceled(true);
            }
        }
    }
}
