package net.tiramisu.noez.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.tiramisu.noez.attribute.NoezAttributes;

import java.util.UUID;

public class Surge extends MobEffect {
    private static final UUID MAGIC_BOOST_UUID = UUID.fromString("f1f5b2f6-3eeb-4bfe-b536-902f8a0e473a");
    public Surge() {
        super(MobEffectCategory.BENEFICIAL, 0x66CCFF);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide()) {
            AttributeInstance attribute = entity.getAttribute(NoezAttributes.MAGIC_DAMAGE.get());
            if (attribute != null && attribute.getModifier(MAGIC_BOOST_UUID) == null) {
                double magicBoostAmount = 15 * amplifier + 5;
                AttributeModifier modifier = new AttributeModifier(MAGIC_BOOST_UUID, "Magic Boost Bonus", magicBoostAmount, AttributeModifier.Operation.ADDITION);
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
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap pAttributeMap, int pAmplifier) {
        AttributeInstance attribute = entity.getAttribute(NoezAttributes.MAGIC_DAMAGE.get());
        if (attribute != null && attribute.getModifier(MAGIC_BOOST_UUID) != null) {
            attribute.removeModifier(MAGIC_BOOST_UUID);
        }
        super.removeAttributeModifiers(entity, pAttributeMap, pAmplifier);
    }
}
