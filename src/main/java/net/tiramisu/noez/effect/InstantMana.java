package net.tiramisu.noez.effect;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.tiramisu.noez.attribute.ManaPlayer;

import javax.annotation.Nullable;

public class InstantMana extends MobEffect {
    public InstantMana() {
        super(MobEffectCategory.BENEFICIAL, 0x66CCFF);
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource, LivingEntity livingEntity, int pAmplifier, double pHealth) {
        if (!livingEntity.level().isClientSide && livingEntity instanceof ServerPlayer serverPlayer) {
            serverPlayer.getCapability(ManaPlayer.MANA).ifPresent(mana -> {
                mana.addMana(2 * pAmplifier + 1);
            });
        }
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }
}
