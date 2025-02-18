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
        if (entity.level().isClientSide) {
            return;
        }
        if (!(entity instanceof Player player)) {
            boolean isTryingToMove = Math.abs(entity.xxa) != 0 || Math.abs(entity.zza) != 0;
            if (isTryingToMove) {
                entity.hurt(entity.damageSources().sweetBerryBush(), 0.5f * amplifier);
            }
            entity.setDeltaMovement(0.0, Math.min(0.0, entity.getDeltaMovement().y), 0.0);
            entity.hurtMarked = true;
        } else {
            player.setJumping(false);
            player.getAbilities().flying = false;
            player.setSwimming(false);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
