package net.tiramisu.noez.event.global;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.util.NoezTags;

@Mod.EventBusSubscriber
public class UndeadSurvival {
    @SubscribeEvent
    public static void undeadProtection(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        Entity attacker =  event.getSource().getEntity();
        if (!entity.level().isClientSide) {
            if (entity.getMobType() == MobType.UNDEAD) {
                var source = event.getSource().getEntity();
                if (source instanceof IronGolem)
                    return;
                if (attacker instanceof LivingEntity livingEntity)
                    if (livingEntity.getMainHandItem().is(NoezTags.Items.UNDEAD_SLAYING))
                        return;
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
