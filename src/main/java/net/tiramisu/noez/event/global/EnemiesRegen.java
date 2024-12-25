package net.tiramisu.noez.event.global;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mod.EventBusSubscriber
public class EnemiesRegen {
    private static final Map<LivingEntity, Long> lastDamageMap = new HashMap<>();
    private static final int DAMAGE_TIMEOUT_MS = 20 * 1000;
    private static final float HEAL_PERCENTAGE = 0.05f;

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity livingEntity = event.getEntity();
            if (livingEntity.getType().getCategory() == MobCategory.MONSTER) {
                lastDamageMap.put(livingEntity, System.currentTimeMillis());
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<LivingEntity, Long>> iterator = lastDamageMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<LivingEntity, Long> entry = iterator.next();
            LivingEntity entity = entry.getKey();
            long lastDamageTime = entry.getValue();
            if (!entity.isAlive() || entity.level().isClientSide()) {
                iterator.remove();
            } else if (currentTime - lastDamageTime >= DAMAGE_TIMEOUT_MS) {
                float maxHealth = entity.getMaxHealth();
                float healAmount = maxHealth * HEAL_PERCENTAGE;
                entity.heal(healAmount);
            }
        }
    }
}
