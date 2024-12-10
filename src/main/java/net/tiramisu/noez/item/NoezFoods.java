package net.tiramisu.noez.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class NoezFoods {
    public static final FoodProperties CURSED_APPLE = new FoodProperties.Builder().alwaysEat().nutrition(8).saturationMod(1.8f).effect(() -> new MobEffectInstance(MobEffects.INVISIBILITY, 15 * 20),1f).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 15 * 20), 1f).build();
}
