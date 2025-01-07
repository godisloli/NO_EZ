package net.tiramisu.noez.item.arsenal;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.tiramisu.noez.item.Critable;

public class MastercraftedWeapon extends SwordItem implements Critable {
    private double CRIT_CHANCE;
    private double CRIT_DAMAGE;

    public MastercraftedWeapon(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, double critChance, double critDamage) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        this.CRIT_CHANCE = critChance;
        this.CRIT_DAMAGE = critDamage;
    }

    @Override
    public double getCritChance() {
        return CRIT_CHANCE;
    }

    @Override
    public double getCritDamageAmplifier() {
        return CRIT_DAMAGE;
    }

    @Override
    public boolean isAlwaysCrit() {
        return false;
    }

    @Override
    public void setAlwaysCrit(boolean value) {

    }
}
