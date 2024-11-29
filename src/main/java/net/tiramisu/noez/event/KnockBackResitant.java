package net.tiramisu.noez.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class KnockBackResitant {

    @SubscribeEvent
    public void onLivingKnockBack(LivingKnockBackEvent event) {
        // Get the entity experiencing knockback
        LivingEntity entity = event.getEntity();

        // Apply knockback resistance on both sides
        if (entity.level().isClientSide()) {
            handleClientSideKnockBack(event, entity);
        } else {
            handleServerSideKnockBack(event, entity);
        }
    }

    private void handleClientSideKnockBack(LivingKnockBackEvent event, LivingEntity entity) {
        // Reduce knockback for visual consistency on the client side
        float reducedStrength = event.getStrength() * 0.35f; // Example: Reduce to 20% of normal
        event.setStrength(reducedStrength);

        double reducedX = event.getRatioX() * 0.35;
        double reducedZ = event.getRatioZ() * 0.35;

        event.setRatioX(reducedX);
        event.setRatioZ(reducedZ);
    }

    private void handleServerSideKnockBack(LivingKnockBackEvent event, LivingEntity entity) {
        // Reduce knockback logic on the server
        float reducedStrength = event.getStrength() * 0.35f; // Example: Reduce to 20% of normal
        event.setStrength(reducedStrength);

        double reducedX = event.getRatioX() * 0.35;
        double reducedZ = event.getRatioZ() * 0.35;

        event.setRatioX(reducedX);
        event.setRatioZ(reducedZ);
    }
}

