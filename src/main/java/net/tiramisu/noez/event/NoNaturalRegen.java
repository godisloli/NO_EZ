package net.tiramisu.noez.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NoNaturalRegen {

    @SubscribeEvent
    public void onLivingHeal(LivingHealEvent event) {
        // Only block natural regeneration for players
        if (event.getEntity() instanceof Player player) {
            // Check if the natural regeneration game rule is disabled
            boolean naturalRegenEnabled = player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);

            // If the game rule is false, check if the healing is natural regeneration (not potion-based or other external sources)
            if (naturalRegenEnabled) {
                // Check if the healing is coming from natural regeneration (i.e., it does not have an external effect like a potion)
                if (event.getAmount() > 0 && isNaturalRegeneration(player)) {
                    // Prevent both client-side and server-side natural regeneration
                    event.setCanceled(true);
                }
            }
        }
    }

    // Determines if the healing is from natural regeneration
    private boolean isNaturalRegeneration(LivingEntity entity) {
        // Natural regeneration typically happens when a player is not hurt and has enough hunger
        // Check if the entity has the regeneration effect (this is a common indicator of natural regen)
        return !entity.hasEffect(MobEffects.REGENERATION); // If they don't have the regen effect, it might be natural regen
    }
}
