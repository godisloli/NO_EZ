package net.tiramisu.noez.attribute;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AttributeHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (isProjectile(event.getSource())) {
            double reduction = livingEntity.getAttributeValue(NoezAttributes.PROJECTILE_REDUCTION.get());
            event.setAmount(applyReduction(event.getAmount(), reduction));
        }
        if (isMagic(event.getSource())) {
            double reduction = livingEntity.getAttributeValue(NoezAttributes.MAGIC_REDUCTION.get());
            event.setAmount(applyReduction(event.getAmount(), reduction));
        }
    }

    private static float applyReduction(float damage, double reduction) {
        return (float) (damage * (1.0 - reduction / 100.0));
    }

    private static boolean isMagic(DamageSource source) {
        return source.getMsgId().equals("magic") || source.getMsgId().equals("indirectMagic");
    }

    private static boolean isProjectile(DamageSource source) {
        return source.getMsgId().equals("arrow") || source.getMsgId().equals("thrown");
    }
}