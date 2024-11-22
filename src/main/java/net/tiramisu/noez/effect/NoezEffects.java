package net.tiramisu.noez.effect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tiramisu.noez.NOEZ;

public class NoezEffects {
    // Create a DeferredRegister for Mob Effects
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, NOEZ.MOD_ID);


    // Method to register the effects with the event bus
    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
