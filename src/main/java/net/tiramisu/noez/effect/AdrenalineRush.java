package net.tiramisu.noez.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class AdrenalineRush extends MobEffect {
    private static final UUID ATTACK_SPEED_MODIFIER_UUID = UUID.fromString("d2766a25-8b45-49d2-98a5-1e9c4dc517d2");

    public AdrenalineRush() {
        super (MobEffectCategory.BENEFICIAL, 0xf1dd30);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide) {
            double attackSpeedBonus = amplifier >= 0 ? (10 + 8 * amplifier) / 100.0 : 0.45;
            if (livingEntity.getAttribute(Attributes.ATTACK_SPEED)
                    .getModifier(ATTACK_SPEED_MODIFIER_UUID) == null) {
                livingEntity.getAttribute(Attributes.ATTACK_SPEED).addPermanentModifier(
                        new AttributeModifier(
                                ATTACK_SPEED_MODIFIER_UUID,
                                "Adrenaline Rush Attack Speed",
                                attackSpeedBonus,
                                AttributeModifier.Operation.MULTIPLY_BASE
                        )
                );
            }
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity, net.minecraft.world.entity.ai.attributes.AttributeMap attributeMap, int amplifier) {
        if (livingEntity.getAttribute(Attributes.ATTACK_SPEED) != null) {
            livingEntity.getAttribute(Attributes.ATTACK_SPEED).removeModifier(ATTACK_SPEED_MODIFIER_UUID);
        }
        super.removeAttributeModifiers(livingEntity, attributeMap, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}

