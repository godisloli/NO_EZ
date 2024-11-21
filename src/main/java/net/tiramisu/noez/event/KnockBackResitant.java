package net.tiramisu.noez.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;

public class KnockBackResitant {
    public void onLivingKnockBack(LivingKnockBackEvent event) {
        // Get the entity experiencing knockback
        LivingEntity entity = event.getEntity();

        // Example: Reduce knockback to 20% of its original value
        float reducedStrength = event.getStrength() * 0.2f;

        // Apply the reduced knockback strength
        event.setStrength(reducedStrength);

        // Optionally modify direction (e.g., slow the pushback speed)
        double reducedX = event.getRatioX() * 0.2;
        double reducedZ = event.getRatioZ() * 0.2;

        event.setRatioX(reducedX);
        event.setRatioZ(reducedZ);
    }
}
