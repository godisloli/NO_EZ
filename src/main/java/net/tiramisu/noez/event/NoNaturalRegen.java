package net.tiramisu.noez.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NoNaturalRegen {

    @SubscribeEvent
    public void onLivingHeal(LivingHealEvent event) {
        // Only block natural regeneration
        if (event.getEntity() instanceof Player player) {
            // Both client-side and server-side logic (avoiding duplicate handling)
            if (player.level().isClientSide()) {
                // Prevent client-side visual health regeneration
                event.setCanceled(true);
            } else {
                // Prevent actual server-side regeneration
                event.setCanceled(true);
            }
        }
    }
}

