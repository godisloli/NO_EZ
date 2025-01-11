package net.tiramisu.noez;

import com.mojang.logging.LogUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.player.Player;
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
import net.tiramisu.noez.attribute.NoezAttributes;
import net.tiramisu.noez.block.NoezBlockEntities;
import net.tiramisu.noez.block.NoezBlocks;
import net.tiramisu.noez.client.renderer.EchoElytraRenderer;
import net.tiramisu.noez.block.CommonVaultRenderer;
import net.tiramisu.noez.client.models.EchoHelmetModel;
import net.tiramisu.noez.client.renderer.EchoHelmetRenderer;
import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.entity.NoezArrowRenderer;
import net.tiramisu.noez.entity.NoezEntities;
import net.tiramisu.noez.entity.GrassSpellShotRenderer;
import net.tiramisu.noez.item.NoezCreativeModTabs;
import net.tiramisu.noez.item.NoezItems;
import net.tiramisu.noez.item.NoezPotions;
import net.tiramisu.noez.network.NoezNetwork;
import net.tiramisu.noez.particles.NoezParticles;
import net.tiramisu.noez.sound.NoezSounds;
import net.tiramisu.noez.util.NoezModelPredicate;
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
        NoezPotions.register(NoezEventBus);
        NoezAttributes.register(NoezEventBus);
        NoezBlockEntities.register(NoezEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(NoezAttributes.class);
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
        new Starter().onServerStart(event,true);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            NoezModelPredicate.itemBowTexturesRenderer(NoezItems.IRIDESCENT_BOW.get());
            NoezModelPredicate.itemBowTexturesRenderer(NoezItems.VOID_STALKER.get());
            NoezModelPredicate.itemCrossbowTexturesRenderer(NoezItems.MECHANICAL_CROSSBOW.get());
            ItemBlockRenderTypes.setRenderLayer(NoezBlocks.COMMON_VAULT.get(), RenderType.translucent());
            BlockEntityRenderers.register(NoezBlockEntities.COMMON_VAULT.get(), CommonVaultRenderer::new);
        }

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(NoezEntities.IRIDESCENT_ARROW.get(), NoezArrowRenderer::new);
            event.registerEntityRenderer(NoezEntities.VOID_ARROW.get(), NoezArrowRenderer::new);
            event.registerEntityRenderer(NoezEntities.ROOT_PROJECTILE.get(), NoezArrowRenderer::new);
            event.registerEntityRenderer(NoezEntities.GRASS_SPELL_SHOT.get(), GrassSpellShotRenderer::new);
            event.registerEntityRenderer(NoezEntities.SOUL_PEARL_ENTITY.get(), ThrownItemRenderer::new);
        }

        @SubscribeEvent
        public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(EchoHelmetRenderer.MODEL, EchoHelmetModel::createBodyModel);
        }

        @SubscribeEvent
        public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
            event.getSkins().forEach(skin -> {
                LivingEntityRenderer<Player, HumanoidModel<Player>> renderer = event.getSkin(skin);
                renderer.addLayer(new EchoHelmetRenderer<>(renderer, event.getEntityModels()));
                renderer.addLayer(new EchoElytraRenderer<>(renderer, event.getEntityModels()));
            });
        }
    }
}
