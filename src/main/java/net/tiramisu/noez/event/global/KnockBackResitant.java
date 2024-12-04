package net.tiramisu.noez.event.global;

import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class KnockBackResitant {

    private static float KNOCKBACK_MODIFIER = 0.35f; // % of original knockback

    @SubscribeEvent
    public static void onLivingKnockBack(LivingKnockBackEvent event) {
        float reducedStrength = event.getStrength() * KNOCKBACK_MODIFIER;
        event.setStrength(reducedStrength);

        double reducedX = event.getRatioX() * KNOCKBACK_MODIFIER;
        double reducedZ = event.getRatioZ() * KNOCKBACK_MODIFIER;

        event.setRatioX(reducedX);
        event.setRatioZ(reducedZ);
    }
}

