package net.tiramisu.noez.item.arsenal;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.tiramisu.noez.item.Critable;

public class BambooSword extends SwordItem implements Critable {
    private static final double CRIT_CHANCE = 0.5;
    private static final double CRIT_DAMAGE = 1.25;
    private boolean ALWAYS_CRIT = false;

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
        return ALWAYS_CRIT;
    }

    @Override
    public void setAlwaysCrit(boolean value) {
        this.ALWAYS_CRIT = value;
    }

    public BambooSword(Tier tier, int Damage, float AttackSpeed, Properties properties) {
        super(tier, Damage, AttackSpeed, properties);
    }
}
