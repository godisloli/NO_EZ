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
import net.tiramisu.noez.particles.particle.*;

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
                spriteSet -> new IridescentHeartParticles.Provider(spriteSet)
        );

        event.registerSpriteSet(
                NoezParticles.BLOOD_SLASH.get(),
                spriteSet -> new BloodSlashParticles.Provider(spriteSet)
        );

        event.registerSpriteSet(
                NoezParticles.BLEED.get(),
                spriteSet -> new BleedParticles.Provider(spriteSet)
        );

        event.registerSpriteSet(
                NoezParticles.ROYAL_EXPLOSION.get(),
                spriteSet -> new RoyalExplosionParticles.Provider(spriteSet)
        );

        event.registerSpriteSet(
                NoezParticles.SYLVAN_ROOT.get(),
                spriteSet -> new SylvanRootParticles.Provider(spriteSet)
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

