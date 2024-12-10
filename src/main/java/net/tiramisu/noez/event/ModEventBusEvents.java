package net.tiramisu.noez.event;

import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.particles.NoezParticles;
import net.tiramisu.noez.particles.particle.ButterflyParticles;
import net.tiramisu.noez.particles.particle.SnowFlakeParticles;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = NOEZ.MOD_ID)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(
                NoezParticles.BUTTERFLY.get(),
                spriteSet -> new ButterflyParticles.Provider(spriteSet)
        );
        event.registerSpriteSet(
                NoezParticles.SNOWFLAKE.get(),
                spriteSet -> new SnowFlakeParticles.Provider(spriteSet)
        );
    }
}

