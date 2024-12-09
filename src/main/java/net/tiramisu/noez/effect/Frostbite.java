package net.tiramisu.noez.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class Frostbite extends MobEffect {
    public Frostbite() {
        super(MobEffectCategory.HARMFUL, 0x99FFFF);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier){
        if (entity.level().isClientSide) {
            return;
        }
        if (entity.isOnFire() && !entity.fireImmune()) {
            entity.removeEffect(this);
            return;
        }
        if (entity.hurtTime > 0 && entity.getLastDamageSource() != null) {
            DamageSource source = entity.getLastDamageSource();
            if (source.getEntity() instanceof LivingEntity) {
                float extraDamage = switch (amplifier) {
                    case 0 -> 1.0f;
                    case 1 -> 1.25f;
                    case 2 -> 1.75f;
                    case 3 -> 2.25f;
                    case 4 -> 3.0f;
                    default -> 3.5f;
                };
                float newHealth = entity.getHealth() - extraDamage;
                if (newHealth < extraDamage) {
                    entity.setHealth(0.0f);
                } else {
                    entity.setHealth(newHealth);
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier){
        return true;
    }
}
