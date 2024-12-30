package net.tiramisu.noez.attribute;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingEvent;
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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity livingEntity = event.getEntity();
            try {
            double healRegen = livingEntity.getAttribute(NoezAttributes.HEALTH_REGENERATION.get()).getValue();
            livingEntity.heal(healPerTick(healRegen));
        } catch (Exception e) {
            System.out.println("No Health Regenerate Attribute for " + livingEntity.getType());
        }

        try {
            double manaRegen = livingEntity.getAttribute(NoezAttributes.MANA_REGENERATION.get()).getValue();
            livingEntity.getCapability(NoezCapacity.MANA).ifPresent(mana ->
                    mana.addFloatMana(manaPerTick(manaRegen)));
        } catch (Exception e) {
            System.out.println("No Mana Regenerate Attribute for " + livingEntity.getType());
        }
    }

    private static float healPerTick(double value) {
        return (float) (value * 0.005);
    }

    private static float manaPerTick(double value) {
        return (float) (value * 0.005);
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