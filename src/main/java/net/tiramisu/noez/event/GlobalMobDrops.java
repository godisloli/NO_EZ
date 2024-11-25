package net.tiramisu.noez.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tiramisu.noez.item.NoezItems;

import java.util.Set;

public class GlobalMobDrops {
    // Blacklisted mobs (by their registry name)
    private static final Set<String> BLACKLISTED_MOBS = Set.of(
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
            return; // Skip processing for blacklisted mobs
        }

        // Ensure the mob is a hostile or neutral mob, not a player
        if (entity.getType().getCategory() == MobCategory.MONSTER || entity.getType().getCategory() == MobCategory.CREATURE) {
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
