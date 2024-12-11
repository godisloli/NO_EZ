package net.tiramisu.noez.event.global;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AthropodsThorn {
    @SubscribeEvent
    public void ThornyAthropods(LivingHurtEvent event){
        LivingEntity target = event.getEntity();
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof LivingEntity)
            if (!target.level().isClientSide() && target.getMobType() == MobType.ARTHROPOD){
                float reflectDamage = event.getAmount() * 0.2f;
                attacker.hurt(target.damageSources().thorns(target), reflectDamage);
        }
    }
}
