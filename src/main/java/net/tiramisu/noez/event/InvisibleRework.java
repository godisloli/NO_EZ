package net.tiramisu.noez.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber
public class InvisibleRework {
    private static final float MIN_INVISIBLE_DURATION = 0.5f; // Minimum duration in seconds

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        handleInvisibility(event.getEntity());
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        handleInvisibility(event.getEntity());
    }

    @SubscribeEvent
    public void onSwing(PlayerInteractEvent.LeftClickEmpty event) {
        handleInvisibility(event.getEntity());
    }

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        handleInvisibility(event.getEntity());
    }

    public static void handleInvisibility(LivingEntity entity) {
        if (entity.hasEffect(MobEffects.INVISIBILITY)) {
            MobEffectInstance effectInstance = entity.getEffect(MobEffects.INVISIBILITY);
            if (effectInstance != null) {
                int currentDuration = effectInstance.getDuration();
                if (currentDuration > MIN_INVISIBLE_DURATION * 20) { // 20 ticks = 1 second
                    entity.removeEffect(MobEffects.INVISIBILITY);
                    entity.addEffect(new MobEffectInstance(
                            MobEffects.INVISIBILITY,
                            (int) (MIN_INVISIBLE_DURATION * 20),
                            0,
                            false,
                            false,
                            true
                    ));
                }
            }
        }
    }
}
