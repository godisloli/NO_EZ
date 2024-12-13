package net.tiramisu.noez.client;

public class ClientManaData {
    private static int mana;
    private static int maxMana;

    public static void set(int newMana) {
        mana = Math.max(0, newMana);
    }

    public static int getMana() {
        return mana;
    }

    public static void setMaxMana(int newMaxMana) {
        maxMana = newMaxMana;
    }

    public static int getMaxMana() {
        return Math.max(0, maxMana);
    }
}

