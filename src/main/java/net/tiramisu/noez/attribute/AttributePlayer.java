package net.tiramisu.noez.attribute;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AttributePlayer implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final Mana mana = new Mana();

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == NoezAttribute.MANA ? LazyOptional.of(() -> mana).cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Mana", mana.getMana());
        tag.putInt("MaxMana", mana.getMaxMana());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        mana.setMana(nbt.getInt("Mana"));
        mana.setMaxMana(nbt.getInt("MaxMana"));
    }
}
