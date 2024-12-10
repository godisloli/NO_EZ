package net.tiramisu.noez.effect;

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
        if (entity.isOnFire()) {
            entity.removeEffect(this);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier){
        return true;
    }
}
