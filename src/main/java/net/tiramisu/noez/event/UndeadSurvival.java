package net.tiramisu.noez.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class UndeadSurvival {
    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();

        if (!entity.level().isClientSide) {
            if (entity.getMobType() == MobType.UNDEAD) {
                var source = event.getSource().getEntity();
                if (source instanceof IronGolem){
                    return;
                }
                float damage = event.getAmount();
                float remainingHealth = entity.getHealth() - damage;
                if (remainingHealth <= 0.0f) {
                    if (!entity.isOnFire()) {
                        entity.setHealth(1.0f);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
