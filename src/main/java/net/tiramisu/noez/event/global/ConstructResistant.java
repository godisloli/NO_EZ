package net.tiramisu.noez.event.global;

import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.util.NoezTags;

@Mod.EventBusSubscriber
public class ConstructResistant {

    @SubscribeEvent
    public void ConstructDamageReduce(LivingHurtEvent event) {
        if (event.getEntity().getType().is(NoezTags.Mobs.CONSTRUCT_MOBS)) {
            event.setAmount(event.getAmount() * 0.8f);
        }
    }
}
