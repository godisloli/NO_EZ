package net.tiramisu.noez.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
    public static void mamgicSurge(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        Entity attackerEntity = source.getEntity();
        if (isMagicDamage(source)) {
            if (attackerEntity instanceof LivingEntity attacker && attacker.hasEffect(NoezEffects.SURGE.get())) {
                int amplifier = attacker.getEffect(NoezEffects.SURGE.get()).getAmplifier() + 1;
                float boostedDamage = event.getAmount() + event.getAmount() * DAMAGE_PER_LEVEL * amplifier;
                event.setAmount(boostedDamage);
            }
        }
    }

    private static boolean isMagicDamage(DamageSource source) {
        return source.getMsgId().equals("magic") || source.getMsgId().equals("indirectMagic");
    }
}
