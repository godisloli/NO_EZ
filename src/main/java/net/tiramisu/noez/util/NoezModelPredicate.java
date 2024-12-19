package net.tiramisu.noez.util;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class NoezModelPredicate {
    public static void itemBowTexturesRenderer(Item item) {
        ItemProperties.register(item, new ResourceLocation("pull"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F :
                        (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
            }
        });
    }

    public static void itemCrossbowTexturesRenderer(Item item) {
        ItemProperties.register(item, new ResourceLocation("pull"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F :
                        (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
            }
        });

        ItemProperties.register(item, new ResourceLocation("pulling"), (stack, world, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        });

        ItemProperties.register(item, new ResourceLocation("firework"), (stack, world, entity, seed) -> {
            return stack.getOrCreateTag().getBoolean("Firework") ? 1.0F : 0.0F;
        });

        ItemProperties.register(item, new ResourceLocation("charged"), (stack, world, entity, seed) -> {
            return stack.getOrCreateTag().getBoolean("Charged") ? 1.0F : 0.0F;
        });

        ItemProperties.register(item, new ResourceLocation("charging"), (stack, world, entity, seed) -> {
            return entity != null && entity.isUsingItem() && !stack.getOrCreateTag().getBoolean("Charged") ? 1.0F : 0.0F;
        });
    }
}
