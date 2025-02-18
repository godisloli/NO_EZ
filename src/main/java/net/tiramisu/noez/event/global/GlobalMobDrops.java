package net.tiramisu.noez.event.global;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.item.NoezItems;

import java.util.Set;

@Mod.EventBusSubscriber
public class GlobalMobDrops {
    private static final Set<String> BLACKLISTED_MOBS = Set.of(
            "minecraft:armor_stand",
            "minecraft:iron_golem",
            "minecraft:slime"
    );

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        String entityId = entity.getType().builtInRegistryHolder().key().location().toString();
        if (BLACKLISTED_MOBS.contains(entityId)) {
            return;
        }
        if (entity instanceof Mob) {
                ItemEntity soulDrop = new ItemEntity(
                        entity.level(),
                        entity.getX(),
                        entity.getY(),
                        entity.getZ(),
                        new ItemStack(NoezItems.SOUL.get())
                );
                event.getDrops().add(soulDrop);
        }
    }
}
