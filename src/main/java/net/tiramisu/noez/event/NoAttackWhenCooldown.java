package net.tiramisu.noez.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class NoAttackWhenCooldown {

    /**
     * Prevents attacking entities if the attack cooldown is not fully charged.
     */
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        // Get the attacking player
        if (event.getEntity() instanceof LocalPlayer player) {
            // Check the attack cooldown
            if (player.getAttackStrengthScale(0.0F) < 1.0F) {
                // Cancel the attack if cooldown is not fully charged
                event.setCanceled(true);
            }
        }
    }

    /**
     * Prevents attack input on client-side when cooldown is not fully charged.
     */
    @SubscribeEvent
    public static void onClickEvent(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player != null && event.isAttack()) {
            // Check if the attack cooldown is less than 1.0
            if (player.getAttackStrengthScale(0.0F) < 1.0F) {
                event.setSwingHand(false); // Prevent the attack swing animation
                event.setCanceled(true);  // Cancel the attack
            }
        }
    }
}
