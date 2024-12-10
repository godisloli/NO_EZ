package net.tiramisu.noez.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class Root extends MobEffect {
    public Root(){
        super(MobEffectCategory.HARMFUL, 0xFFFF00);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        if (entity.level().isClientSide) {
            return;
        }
        entity.setDeltaMovement(0.0, Math.min(0.0, entity.getDeltaMovement().y), 0.0);
        if (entity instanceof Player player) {
            player.setJumping(false);
            player.getAbilities().flying = false;
            player.setSwimming(false);
        }
        entity.hurtMarked = true;
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
