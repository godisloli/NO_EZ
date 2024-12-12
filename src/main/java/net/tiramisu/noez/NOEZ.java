package net.tiramisu.noez;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tiramisu.noez.attribute.NoezAttribute;
import net.tiramisu.noez.block.NoezBlocks;
import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.entity.NoezArrowRenderer;
import net.tiramisu.noez.entity.NoezEntities;
import net.tiramisu.noez.event.global.*;
import net.tiramisu.noez.item.NoezCreativeModTabs;
import net.tiramisu.noez.item.NoezItems;
import net.tiramisu.noez.network.NoezNetwork;
import net.tiramisu.noez.particles.NoezParticles;
import net.tiramisu.noez.sound.NoezSounds;
import net.tiramisu.noez.util.NoezItemProperties;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixins;
import net.tiramisu.noez.event.serverstarting.Starter;

@Mod(NOEZ.MOD_ID)
public class NOEZ
{
    static {
        Mixins.addConfiguration("mixins.noez.json");
    }

    public static final String MOD_ID = "noez";

    private static final Logger LOGGER = LogUtils.getLogger();

    public NOEZ(FMLJavaModLoadingContext context)
    {
        IEventBus NoezEventBus = context.getModEventBus();
        NoezCreativeModTabs.register(NoezEventBus);
        NoezItems.register(NoezEventBus);
        NoezBlocks.register(NoezEventBus);
        NoezEntities.register(NoezEventBus);
        NoezParticles.register(NoezEventBus);
        NoezEffects.register(NoezEventBus);
        NoezSounds.register(NoezEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new NoMiningToolBreak());
        MinecraftForge.EVENT_BUS.register(new LineOfSight());
        MinecraftForge.EVENT_BUS.register(new NoAttackWhenCooldown());
        MinecraftForge.EVENT_BUS.register(new KnockBackResitant());
        MinecraftForge.EVENT_BUS.register(new GlobalMobDrops());
        MinecraftForge.EVENT_BUS.register(new UndeadSurvival());
        MinecraftForge.EVENT_BUS.register(new AthropodsThorn());
        MinecraftForge.EVENT_BUS.register(new InvisibleRework());
        MinecraftForge.EVENT_BUS.register(new EnemiesRegen());
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        NoezEventBus.addListener(this::addCreative);
        NoezEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(NoezNetwork::registerPackets);
    }


    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {

        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        new Starter().DisableNaturalRegeneration(event,true);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            NoezItemProperties.addCustomBowProperties(NoezItems.IRIDESCENT_BOW.get());
        }

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(NoezEntities.IRIDESCENT_ARROW.get(),NoezArrowRenderer::new);
            event.registerEntityRenderer(NoezEntities.ROOT_PROJECTILE.get(),NoezArrowRenderer::new);
        }
    }
}
