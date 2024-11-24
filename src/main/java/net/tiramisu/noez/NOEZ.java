package net.tiramisu.noez;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
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
import net.tiramisu.noez.block.NoezBlocks;
import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.event.*;
import net.tiramisu.noez.item.NoezCreativeModTabs;
import net.tiramisu.noez.item.NoezItems;
import net.tiramisu.noez.mixin.LivingEntityMixin;
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
        IEventBus modEventBus = context.getModEventBus();

        NoezCreativeModTabs.register(modEventBus);

        NoezItems.register(modEventBus);

        NoezBlocks.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        NoezEffects.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(new LineOfSight());

        MinecraftForge.EVENT_BUS.register(new NoAttackWhenCooldown());

        MinecraftForge.EVENT_BUS.register(new KnockBackResitant());

        MinecraftForge.EVENT_BUS.register(new GlobalMobDrops());

        MinecraftForge.EVENT_BUS.register(new UndeadSurvival());

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }


    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(NoezItems.SOUL);
            event.accept(NoezItems.RUINOUSSOUL);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        new Starter().DisableNaturalRegeneration(event,true);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
