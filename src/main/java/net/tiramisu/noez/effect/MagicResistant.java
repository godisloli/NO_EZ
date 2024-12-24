package net.tiramisu.noez.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.NOEZ;

@Mod.EventBusSubscriber(modid = NOEZ.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MagicResistant extends MobEffect {
    private static final float REDUCED_PERCENTAGE = 0.15f;

    public MagicResistant() {
        super (MobEffectCategory.BENEFICIAL, 0x1140e0);
    }

    @SubscribeEvent
    public void magicResist (LivingHurtEvent event) {
        Entity entity = event.getEntity();
        DamageSource source = event.getSource();
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(NoezEffects.MAGIC_RESISTANT.get())) {
            int amplifier = Math.min(Math.max(livingEntity.getEffect(NoezEffects.MAGIC_RESISTANT.get()).getAmplifier(), 1) ,6);
            float reducedDamage = event.getAmount() * (1 - REDUCED_PERCENTAGE * amplifier);
            if (isMagic(source)) {
                event.setAmount(reducedDamage);
            }
        }
    }

    private boolean isMagic(DamageSource source) {
        return source.getMsgId().equals("magic") || source.getMsgId().equals("indirectMagic");
    }
}
