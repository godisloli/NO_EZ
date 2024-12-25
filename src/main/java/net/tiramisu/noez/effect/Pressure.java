package net.tiramisu.noez.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Pressure extends MobEffect {
    public Pressure() {
        super(MobEffectCategory.HARMFUL, 0x152C39);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof Player player && !player.level().isClientSide() && !player.getAbilities().instabuild) {
            player.setAirSupply(player.getAirSupply() - 2);
            player.hurtMarked = true;
        }
    }

    @SubscribeEvent
    public static void drowning(LivingBreatheEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity.hasEffect(NoezEffects.PRESSURE.get())) {
            event.setRefillAirAmount(0);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int effectiveAmplifier = Math.max(pAmplifier + 1, 1);
        int tickInterval = Math.max(20 / effectiveAmplifier, 1);
        return pDuration % tickInterval == 0;
    }
}
