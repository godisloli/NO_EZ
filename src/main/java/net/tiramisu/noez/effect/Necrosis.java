package net.tiramisu.noez.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class Necrosis extends MobEffect {
    private static final UUID MAX_HEALTH_UUID = UUID.fromString("11111111-2222-3333-4444-555555555555");

    public Necrosis() {
        super(MobEffectCategory.HARMFUL, 0x330000);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(entity, attributeMap, amplifier);

        if (!entity.level().isClientSide() && entity.getAttribute(Attributes.MAX_HEALTH) != null) {
            double reductionPercent = 0.1 * (amplifier + 1);
            AttributeModifier modifier = new AttributeModifier(
                    MAX_HEALTH_UUID,
                    "Necrosis health reduction",
                    -reductionPercent,
                    AttributeModifier.Operation.MULTIPLY_BASE
            );
            if (entity.getAttribute(Attributes.MAX_HEALTH).getModifier(MAX_HEALTH_UUID) == null) {
                entity.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(modifier);
            }
            if (entity.getHealth() > entity.getMaxHealth()) {
                entity.setHealth(entity.getMaxHealth());
            }
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity,attributeMap, amplifier);
        if (entity.getAttribute(Attributes.MAX_HEALTH) != null) {
            entity.getAttribute(Attributes.MAX_HEALTH).removeModifier(MAX_HEALTH_UUID);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }
}
