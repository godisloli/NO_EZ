package net.tiramisu.noez.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.block.ModBlocks;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NOEZ.MOD_ID);

    public static final RegistryObject<CreativeModeTab> NOEZ_TAB = CREATIVE_MODE_TABS.register("noez_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SOUL.get()))
                    .title(Component.translatable("creativetab.noez_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.SOUL.get());
                        pOutput.accept(ModItems.RUINOUSSOUL.get());
                        pOutput.accept(ModBlocks.SOUL_BLOCK.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
