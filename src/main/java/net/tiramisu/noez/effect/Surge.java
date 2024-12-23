package net.tiramisu.noez.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Surge extends MobEffect {
    private static final float DAMAGE_PER_LEVEL = 0.25f;

    public Surge() {
        super(MobEffectCategory.BENEFICIAL, 0x66CCFF);
    }

    @SubscribeEvent
    public void mamgicSurge(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        System.out.println("Damage Source: " + source.getMsgId());
        Entity attackerEntity = source.getEntity();
        Entity directEntity = source.getDirectEntity();
        if (isMagicDamage(source)) {
            if (attackerEntity instanceof LivingEntity attacker && attacker.hasEffect(this)) {
                int amplifier = attacker.getEffect(this).getAmplifier() + 1;
                float boostedDamage = event.getAmount() * (1 + (DAMAGE_PER_LEVEL * amplifier));
                event.setAmount(boostedDamage);
            }
            if (directEntity instanceof ThrownPotion potion && potion.getOwner() instanceof LivingEntity thrower) {
                if (thrower.hasEffect(this)) {
                    int amplifier = thrower.getEffect(this).getAmplifier() + 1;
                    float boostedDamage = event.getAmount() * (1 + (DAMAGE_PER_LEVEL * amplifier));
                    event.setAmount(boostedDamage);
                }
            }
        }
    }

    private boolean isMagicDamage(DamageSource source) {
        return source.getMsgId().equals("magic") || source.getMsgId().equals("indirectMagic");
    }
}
