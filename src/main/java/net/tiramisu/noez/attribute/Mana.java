package net.tiramisu.noez.attribute;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.tiramisu.noez.network.packet.ManaDataSyncS2CPacket;
import net.tiramisu.noez.network.NoezNetwork;

public class Mana {
    private int mana;
    private int maxMana;
    private ServerPlayer owner;
    private float temporaryMana = 0;

    public Mana() {
        this.mana = 20;
        this.maxMana = 20;
    }

    public void setOwner(ServerPlayer owner) {
        this.owner = owner;
    }

    private void syncMana() {
        if (owner != null && !owner.level().isClientSide) {
            NoezNetwork.sendDataToClient(owner, new ManaDataSyncS2CPacket(mana));
        }
    }

    public boolean isEmpty() {
        return mana == 0;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(mana, maxMana));
        syncMana();
    }

    public void addMana(int amount) {
        setMana(Math.min(maxMana, this.mana + amount));
    }

    public void addFloatMana(float amount) {
        float totalMana = temporaryMana + amount;
        if (totalMana < 1) {
            this.temporaryMana = totalMana;
        } else {
            addMana((int) totalMana);
            this.temporaryMana = totalMana - (int) totalMana;
        }
    }

    public void consumeMana(int amount) {
        setMana(Math.max(0, this.mana - amount));
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public void copyFrom(Mana source) {
        this.mana = source.mana;
        this.maxMana = source.maxMana;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("mana", mana);
    }

    public void loadNBTData(CompoundTag nbt) {
        mana = nbt.getInt("mana");
    }
}
