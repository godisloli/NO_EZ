package net.tiramisu.noez.particles;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tiramisu.noez.NOEZ;

public class NoezParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, NOEZ.MOD_ID);

    public static final RegistryObject<SimpleParticleType> BUTTERFLY = PARTICLES.register("butterfly",
            () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> SNOWFLAKE = PARTICLES.register("snowflake",
            () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> WINDBLOW = PARTICLES.register("windblow",
            () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> IRIDESCENT_HEART = PARTICLES.register("iridescent_heart",
            () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLES.register(eventBus);
    }
}
