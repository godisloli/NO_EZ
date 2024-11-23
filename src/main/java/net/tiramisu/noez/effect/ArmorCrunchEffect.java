package net.tiramisu.noez.effect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;

public class ArmorCrunchEffect extends MobEffect {

    public ArmorCrunchEffect() {
        super(MobEffectCategory.HARMFUL, 0x2B6A10); // Red, yellowish color for the effect
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Monster) {  // Apply only to mobs, or add condition for specific entities
            // Check if the entity has armor and reduce it
            if (entity.getArmorValue() > 0) {
                int newArmorValue = entity.getArmorValue() - (amplifier + 1); // Decrease armor based on effect level
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // This effect applies every tick (1/20th of a second)
        return true;
    }
}

