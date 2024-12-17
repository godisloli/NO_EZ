package net.tiramisu.noez.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Wound extends MobEffect {
    public Wound() {
        super(MobEffectCategory.HARMFUL, 0xFF0000);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void antiHeal(LivingHealEvent event) {
        if (event.getEntity().hasEffect(this)) {
            event.setCanceled(true);
        }
    }
}
