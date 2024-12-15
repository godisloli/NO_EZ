package net.tiramisu.noez.effect;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.tiramisu.noez.attribute.ManaPlayer;

public class Regain extends MobEffect {
    public Regain() {
        super(MobEffectCategory.BENEFICIAL, 0x79BAEC);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof ServerPlayer serverPlayer){
            serverPlayer.getCapability(ManaPlayer.MANA).ifPresent(mana -> {
                mana.addMana(Math.max(1, amplifier));
            });
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }
}
