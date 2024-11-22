package net.tiramisu.noez.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class UndeadSurvival {
    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        // Get the entity taking damage
        LivingEntity entity = event.getEntity();

        // Only execute on the server side
        if (!entity.level().isClientSide) {
            // Check if the entity is undead
            if (entity.getMobType() == MobType.UNDEAD) {
                float damage = event.getAmount();
                float remainingHealth = entity.getHealth() - damage;

                // If the damage would kill the entity
                if (remainingHealth <= 0.0f) {
                    // Check if the entity is burning
                    if (!entity.isOnFire()) {
                        // Prevent death by setting health to 1
                        entity.setHealth(1.0f);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
