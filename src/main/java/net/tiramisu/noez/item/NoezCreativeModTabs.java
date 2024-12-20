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

    public static final RegistryObject<CreativeModeTab> NOEZ_TAB = CREATIVE_MODE_TABS.register("noez_tab_misc",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NoezItems.SOUL.get()))
                    .title(Component.translatable("creativetab.noez_tab_misc"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(NoezItems.SOUL.get());
                        pOutput.accept(NoezBlocks.SOUL_BLOCK.get());
                        pOutput.accept(NoezItems.INFUSED_COPPER.get());
                        pOutput.accept(NoezItems.OBSIDIAN_SHARD.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> NOEZ_TAB_FOOD = CREATIVE_MODE_TABS.register("noez_tab_food",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NoezItems.FRUIT_OF_DECEPTION.get()))
                    .title(Component.translatable("creativetab.noez_tab_food"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(NoezItems.FRUIT_OF_DECEPTION.get());
                        pOutput.accept(NoezItems.MANA_POTION.get());
                        pOutput.accept(NoezItems.VITAL_WRAP.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> NOEZ_TAB_COMBAT = CREATIVE_MODE_TABS.register("noez_tab_combat",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NoezItems.WINDBREAKER.get()))
                    .title(Component.translatable("creativetab.noez_tab_combat"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(NoezItems.DRUVIS_STAFF.get());
                        pOutput.accept(NoezItems.NATURA_BLADE.get());
                        pOutput.accept(NoezItems.OBSIDIAN_BLADE.get());
                        pOutput.accept(NoezItems.HELLFIRE_SWORD.get());
                        pOutput.accept(NoezItems.FROSTBANE_SWORD.get());
                        pOutput.accept(NoezItems.IRIDESCENT_BOW.get());
                        pOutput.accept(NoezItems.RELOCATOR.get());
                        pOutput.accept(NoezItems.WINDBREAKER.get());
                        pOutput.accept(NoezItems.BAMBOO_SWORD.get());
                        pOutput.accept(NoezItems.DARK_DEVASTATION.get());
                        pOutput.accept(NoezItems.MOONSILVER_SWORD.get());
                        pOutput.accept(NoezItems.BLOOD_SACRIFICE.get());
                        pOutput.accept(NoezItems.MECHANICAL_CROSSBOW.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
