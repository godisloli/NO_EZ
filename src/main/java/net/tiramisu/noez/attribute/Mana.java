package net.tiramisu.noez.attribute;

public class Mana {
    private int mana;
    private int maxMana;
    public Mana() {
        this.mana = 20; // Default mana value
        this.maxMana = 20; // Default maximum mana
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(mana, maxMana)); // Clamp between 0 and maxMana
    }

    public void addMana(int amount) {
        setMana(this.mana + amount);
    }

    public void consumeMana(int amount) {
        setMana(this.mana - amount);
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }
}
