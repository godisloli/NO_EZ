package net.tiramisu.noez.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Surge extends MobEffect {
    private static final float DAMAGE_PER_LEVEL = 0.25f;

    public Surge() {
        super(MobEffectCategory.BENEFICIAL, 0x66CCFF);
    }

    @SubscribeEvent
    public void mamgicSurge(LivingAttackEvent event) {
        Entity entity = event.getSource().getEntity();
        if (entity instanceof LivingEntity attacker) {
            if (attacker.hasEffect(this)) {
                if (isMagicDamage(event.getSource())) {
                    int amplifier = attacker.getEffect(this).getAmplifier() + 1;
                    float boostedDamage = event.getAmount() * (1 + (DAMAGE_PER_LEVEL * amplifier));
                    event.setCanceled(true);
                    event.getEntity().hurt(attacker.damageSources().magic(), boostedDamage);
                }
            }
        }
    }

    private boolean isMagicDamage(DamageSource source) {
        return source.getMsgId().equals("magic") || source.getMsgId().equals("indirectMagic");
    }
}
