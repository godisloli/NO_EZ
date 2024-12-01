package net.tiramisu.noez.event.global;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class KnockBackResitant {

    private float KNOCKBACK_MODIFIER = 3.5f; // % of original knockback

    @SubscribeEvent
    public void onLivingKnockBack(LivingKnockBackEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) {
            handleClientSideKnockBack(event, entity);
        } else {
            handleServerSideKnockBack(event, entity);
        }
    }

    private void handleClientSideKnockBack(LivingKnockBackEvent event, LivingEntity entity) {
        float reducedStrength = event.getStrength() * KNOCKBACK_MODIFIER;
        event.setStrength(reducedStrength);
        double reducedX = event.getRatioX() * KNOCKBACK_MODIFIER;
        double reducedZ = event.getRatioZ() * KNOCKBACK_MODIFIER;

        event.setRatioX(reducedX);
        event.setRatioZ(reducedZ);
    }

    private void handleServerSideKnockBack(LivingKnockBackEvent event, LivingEntity entity) {
        double reducedX = event.getRatioX() * KNOCKBACK_MODIFIER;
        double reducedZ = event.getRatioZ() * KNOCKBACK_MODIFIER;
        event.setRatioX(reducedX);
        event.setRatioZ(reducedZ);
    }
}

