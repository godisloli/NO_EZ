package net.tiramisu.noez.item;

public interface Critable {
    double getCritChance();
    double getCritDamageAmplifier();
    boolean isAlwaysCrit();
    void setAlwaysCrit(boolean value);
}
