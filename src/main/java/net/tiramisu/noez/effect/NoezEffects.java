package net.tiramisu.noez.effect;
import com.mojang.blaze3d.shaders.Effect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tiramisu.noez.NOEZ;

public class NoezEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, NOEZ.MOD_ID);

    public static final RegistryObject<MobEffect> BLEED = EFFECTS.register("bleed",Bleed::new);

    public static final RegistryObject<MobEffect> STUN = EFFECTS.register("stun",Stun::new);

    public static final RegistryObject<MobEffect> ROOT = EFFECTS.register("root", Root::new);

    public static final RegistryObject<MobEffect> FROSTBITE = EFFECTS.register("frostbite", Frostbite::new);

    public static final RegistryObject<MobEffect> ARMOR_CRUNCH = EFFECTS.register("armor_crunch", ArmorCrunch::new);

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
