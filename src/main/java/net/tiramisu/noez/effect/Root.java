package net.tiramisu.noez.effect;

import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

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
        boolean isTryingToMove = Math.abs(entity.xxa) != 0 || Math.abs(entity.zza) != 0;
        if (isTryingToMove){
            entity.hurt(entity.damageSources().cactus(), 0.5f * amplifier);
        }
        if (entity instanceof Player player) {
            player.setJumping(false);
            player.getAbilities().flying = false;
            player.setSwimming(false);
        }
        entity.setDeltaMovement(0.0, Math.min(0.0, entity.getDeltaMovement().y), 0.0);
        entity.hurtMarked = true;
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
