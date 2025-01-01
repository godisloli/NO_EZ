package net.tiramisu.noez.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.tiramisu.noez.particles.NoezParticles;
import net.tiramisu.noez.util.NoezTags;

public class Bleed extends MobEffect {
    public Bleed(){
        super(MobEffectCategory.HARMFUL, 0xFF0000);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide & !entity.getType().is(NoezTags.Mobs.CONSTRUCT_MOBS)) {
            double damage = getDamagePerLevel(amplifier);
            entity.hurt(entity.damageSources().magic(), (float) damage);
            if (entity.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        NoezParticles.BLEED.get(),
                        entity.getX(),
                        entity.getY() + 0.5,
                        entity.getZ(),
                        3,
                        0.1, 0.1, 0.1,
                        0.1
                );
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    private double getDamagePerLevel(int amplifier) {
        switch (amplifier) {
            case 0: return 1.0;
            case 1: return 1.25;
            case 2: return 1.5;
            case 3: return 2.0;
            case 4: return 2.5;
            default: return 4.0;
        }
    }
}

