package net.tiramisu.noez.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.tiramisu.noez.attribute.NoezAttributes;

import java.util.UUID;

public class MagicResistant extends MobEffect {
    private static final UUID MAGIC_RESISTANT_UUID = UUID.fromString("f1f4b2f3-3eeb-4bfe-b536-902f8a0e473a");

    private static final double REDUCED_PERCENTAGE = 15;

    public MagicResistant() {
        super (MobEffectCategory.BENEFICIAL, 0x1140e0);
    }
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide()) {
            AttributeInstance attribute = entity.getAttribute(NoezAttributes.MAGIC_REDUCTION.get());
            if (attribute != null && attribute.getModifier(MAGIC_RESISTANT_UUID) == null) {
                double magicResistAmount = Math.min(80, (amplifier + 1) * REDUCED_PERCENTAGE);
                AttributeModifier modifier = new AttributeModifier(MAGIC_RESISTANT_UUID, "Magic Resist Bonus", magicResistAmount, AttributeModifier.Operation.ADDITION);
                attribute.addPermanentModifier(modifier);
            }
        }
        super.applyEffectTick(entity, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        AttributeInstance attribute = entity.getAttribute(NoezAttributes.MAGIC_REDUCTION.get());
        if (attribute != null && attribute.getModifier(MAGIC_RESISTANT_UUID) != null) {
            attribute.removeModifier(MAGIC_RESISTANT_UUID);
        }
        super.removeAttributeModifiers(entity, attributes, amplifier);
    }
}
