package net.tiramisu.noez.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class NoezFoods extends Item {
    public static final FoodProperties FRUIT_OF_DECEPTION = new FoodProperties.Builder()
            .alwaysEat()
            .nutrition(4)
            .saturationMod(1.2f)
            .effect(() -> new MobEffectInstance(MobEffects.INVISIBILITY, 60 * 20),1f)
            .effect(() -> new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 30 * 20, 2), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30 * 20, 2), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3), 1.0F)
            .build();
    private boolean isFOIL = false;

    public NoezFoods(Properties properties){
        super (properties);
    }

    @Override
    public boolean isFoil(ItemStack itemStack){
        return isFOIL;
    }

    public NoezFoods setFOIL(boolean FOIL) {
        this.isFOIL = FOIL;
        return this;
    }
}
