package net.tiramisu.noez.effect;

import net.minecraft.client.gui.screens.controls.KeyBindsList;
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
                entity.hurt(entity.damageSources().cactus(), 0.5f * amplifier);
            }
            entity.setDeltaMovement(0.0, Math.min(0.0, entity.getDeltaMovement().y), 0.0);
        } else {
            player.setJumping(false);
            player.getAbilities().flying = false;
            player.setSwimming(false);
            if (player.getDeltaMovement().x != 0.0 || player.getDeltaMovement().z != 0.0)
                player.hurt(player.damageSources().cactus(), 0.5f * amplifier);
            System.out.println("x = " + player.getDeltaMovement().x + " y = " + player.getDeltaMovement().y + " z = " + player.getDeltaMovement().z);
            System.out.println("xxa = " + player.xxa + " zza = " + player.zza);
        }
        entity.hurtMarked = true;
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
