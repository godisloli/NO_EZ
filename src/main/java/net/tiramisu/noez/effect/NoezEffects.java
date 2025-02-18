package net.tiramisu.noez.effect;

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

    public static final RegistryObject<MobEffect> CORRUPTION = EFFECTS.register("corruption", Corruption::new);

    public static final RegistryObject<MobEffect> SURGE = EFFECTS.register("surge", Surge::new);

    public static final RegistryObject<MobEffect> REGAIN = EFFECTS.register("regain", Regain::new);

    public static final RegistryObject<MobEffect> ADRENALINE_RUSH = EFFECTS.register("adrenaline_rush", AdrenalineRush::new);

    public static final RegistryObject<MobEffect> WOUND = EFFECTS.register("wound", Wound::new);

    public static final RegistryObject<MobEffect> MAGIC_RESISTANT = EFFECTS.register("magic_resistant", MagicResistant::new);

    public static final RegistryObject<MobEffect> PRESSURE = EFFECTS.register("pressure", Pressure::new);

    public static final RegistryObject<MobEffect> NECROSIS = EFFECTS.register("necrosis", Necrosis::new);


    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
