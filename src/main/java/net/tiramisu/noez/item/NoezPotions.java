package net.tiramisu.noez.item;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tiramisu.noez.NOEZ;

public class NoezPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, NOEZ.MOD_ID);

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
