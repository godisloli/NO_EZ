package net.tiramisu.noez.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class ArmorCrunch extends MobEffect {
    public ArmorCrunch(){
        super (MobEffectCategory.HARMFUL, 0xFF4500);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // Only apply to players or entities with armor (skip mobs without armor)
        if (entity instanceof Player) {
            Player player = (Player) entity;

            // Calculate armor reduction based on effect level
            double armorReduction = calculateArmorReduction(amplifier);

            // Store the original armor value
            if (!player.getPersistentData().contains("OriginalArmor")) {
                player.getPersistentData().putDouble("OriginalArmor", player.getArmorValue());
            }

            // Apply the armor reduction
            double newArmor = Math.max(0, player.getArmorValue() - armorReduction);
            player.getAttribute(Attributes.ARMOR).setBaseValue(newArmor);
        }
    }

    public void onRemove(LivingEntity entity) {
        // Called when the effect is removed, either expired or cleared
        if (entity instanceof Player) {
            Player player = (Player) entity;

            // Restore original armor value
            if (player.getPersistentData().contains("OriginalArmor")) {
                double originalArmor = player.getPersistentData().getDouble("OriginalArmor");
                player.getAttribute(Attributes.ARMOR).setBaseValue(originalArmor);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // This ensures that the effect occurs every tick (20 ticks per second)
        return duration % 20 == 0; // Every second
    }

    // Helper to calculate armor reduction based on the effect's level
    private double calculateArmorReduction(int amplifier) {
        switch (amplifier) {
            case 0: return 1.0;  // Level 1: Reduce armor by 1
            case 1: return 1.5;  // Level 2: Reduce armor by 1.5
            case 2: return 2.0;  // Level 3: Reduce armor by 2
            case 3: return 2.5;  // Level 4: Reduce armor by 2.5
            case 4: return 3.0;  // Level 5: Reduce armor by 3
            default: return 0.0;  // Default: No reduction
        }
    }
}
