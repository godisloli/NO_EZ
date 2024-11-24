package net.tiramisu.noez.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class Bleed extends MobEffect {
    public Bleed(){
        super(MobEffectCategory.HARMFUL, 0xFF0000);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // Apply damage over time every tick
        if (!entity.level().isClientSide) {
            double damage = getDamagePerLevel(amplifier);
            entity.hurt(entity.damageSources().magic(), (float) damage);  // Apply magic damage
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    private double getDamagePerLevel(int amplifier) {
        // Return damage based on the level (amplifier)
        switch (amplifier) {
            case 0: return 1.0;
            case 1: return 1.5;
            case 2: return 2.0;
            case 3: return 2.5;
            case 4: return 3.0;
            default: return 5.0; // Maximum damage if level > 4
        }
    }
}

