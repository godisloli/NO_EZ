package net.tiramisu.noez.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tiramisu.noez.item.NoezItems;

import java.util.Arrays;
import java.util.List;

public class GlobalMobDrops {
    private static final List<String> BLACKLISTED_MOBS = Arrays.asList(
            "minecraft:armor_stand",
            "minecraft:iron_golem"
    );

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        // Get the entity that died
        LivingEntity entity = event.getEntity();

        // Check if the mob is blacklisted
        String entityId = entity.getType().builtInRegistryHolder().key().location().toString();
        if (BLACKLISTED_MOBS.contains(entityId)) {
            return; // Do not drop the item if the mob is blacklisted
        }

        // Ensure the mob is a hostile or neutral mob, not a player
        if (entity.getType().getCategory() == MobCategory.MONSTER || entity.getType().getCategory() == MobCategory.CREATURE) {
            // Check if the mob was killed by a player, or always drop
            if (event.getSource().getEntity() instanceof Player || event.isCanceled()) {
                // Create and add the custom item to drops
                event.getDrops().add(new net.minecraft.world.entity.item.ItemEntity(
                        entity.level(),
                        entity.getX(),
                        entity.getY(),
                        entity.getZ(),
                        new ItemStack(NoezItems.SOUL.get())
                ));
            }
        }
    }
}
