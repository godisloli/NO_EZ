package net.tiramisu.noez.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.particles.NoezParticles;

import java.util.Random;

public class Frostbite extends MobEffect {
    public Frostbite() {
        super(MobEffectCategory.HARMFUL, 0x99FFFF);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        Level level = entity.level();
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        if (entity.isOnFire()) {
            entity.removeEffect(this);
            return;
        }
        Random random = new Random();
        double offsetX = (random.nextDouble() - 0.5) * (entity.getBbWidth() + 0.5);
        double offsetY = random.nextDouble() * (entity.getBbHeight() + 0.5);
        double offsetZ = (random.nextDouble() - 0.5) * (entity.getBbWidth() + 0.5);
        for (int i = 0; i < 3; i++) {
            serverLevel.sendParticles(
                    NoezParticles.SNOWFLAKE.get(),
                    entity.getX() + offsetX,
                    entity.getY() + offsetY,
                    entity.getZ() + offsetZ,
                    1,
                    0, 0, 0,
                    0.1
            );
        }
    }


    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int tickInterval = 10 >> amplifier;
        return duration % Math.max(tickInterval, 1) == 0;
    }
}
