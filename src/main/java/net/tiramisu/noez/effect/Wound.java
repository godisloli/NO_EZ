package net.tiramisu.noez.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Wound extends MobEffect {
    public Wound() {
        super(MobEffectCategory.HARMFUL, 0xFF0000);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void antiHeal(LivingHealEvent event) {
        float amount = event.getAmount();
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(NoezEffects.WOUND.get())) {
            int amplifier = livingEntity.getEffect(NoezEffects.WOUND.get()).getAmplifier();
            float reduction = (amount * Math.min(0.95f, (amplifier + 1) * 0.15f));
            event.setAmount(amount - reduction);
        }
    }
}
