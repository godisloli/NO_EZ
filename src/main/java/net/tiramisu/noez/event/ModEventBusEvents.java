package net.tiramisu.noez.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.attribute.ManaPlayer;
import net.tiramisu.noez.network.NoezNetwork;
import net.tiramisu.noez.particles.NoezParticles;
import net.tiramisu.noez.particles.particle.ButterflyParticles;
import net.tiramisu.noez.particles.particle.IridescentHeart;
import net.tiramisu.noez.particles.particle.SnowFlakeParticles;
import net.tiramisu.noez.particles.particle.WindBlowParticles;

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

        event.registerSpriteSet(
                NoezParticles.WINDBLOW.get(),
                spriteSet -> new WindBlowParticles.Provider(spriteSet)
        );

        event.registerSpriteSet(
                NoezParticles.IRIDESCENT_HEART.get(),
                spriteSet -> new IridescentHeart.Provider(spriteSet)
        );
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinLevelEvent event){
        if (!event.getLevel().isClientSide()){
            if (event.getEntity() instanceof ServerPlayer serverPlayer){
                serverPlayer.getCapability(ManaPlayer.MANA).ifPresent(mana -> {
                    NoezNetwork.sendDataToClient(serverPlayer, mana.getMana());
                });
            }
        }
    }
}

