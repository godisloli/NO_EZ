package net.tiramisu.noez.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.block.NoezBlocks;

public class NoezCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NOEZ.MOD_ID);

    public static final RegistryObject<CreativeModeTab> NOEZ_TAB = CREATIVE_MODE_TABS.register("noez_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NoezItems.SOUL.get()))
                    .title(Component.translatable("creativetab.noez_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(NoezItems.SOUL.get());
                        pOutput.accept(NoezBlocks.SOUL_BLOCK.get());
                        pOutput.accept(NoezItems.RELOCATOR.get());
                        pOutput.accept(NoezItems.NATURA_BLADE.get());
                        pOutput.accept(NoezItems.INFUSED_COPPER.get());
                        pOutput.accept(NoezItems.OBSIDIAN_SHARD.get());
                        pOutput.accept(NoezItems.IRIDESCENT_BOW.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
