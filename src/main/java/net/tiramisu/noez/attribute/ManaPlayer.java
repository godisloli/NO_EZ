package net.tiramisu.noez.attribute;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ManaPlayer implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private Mana mana = null;
    public static final Capability<Mana> MANA = CapabilityManager.get(new CapabilityToken<>() {});
    private final LazyOptional<Mana> optional = LazyOptional.of(this::createMana);

    private Mana createMana() {
        if (this.mana == null) {
            this.mana = new Mana();
        }
        return this.mana;
    }

    public void setOwner(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            createMana().setOwner(serverPlayer);
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == MANA ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        createMana().saveNBTData(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createMana().loadNBTData(nbt);
    }
}
