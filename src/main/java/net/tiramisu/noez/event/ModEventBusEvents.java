package net.tiramisu.noez.event;

import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.NOEZ;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = NOEZ.MOD_ID)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        // Register the particle factory for the BUTTERFLY particle
        event.registerSpriteSet(
                net.tiramisu.noez.particles.NoezParticles.BUTTERFLY.get(),
                spriteSet -> new net.tiramisu.noez.particles.particle.Butterfly_Particles.Provider(spriteSet)
        );
    }
}

