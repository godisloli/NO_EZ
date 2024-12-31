package net.tiramisu.noez.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
                        pOutput.accept(NoezItems.SCULK_BONE.get());
                        pOutput.accept(NoezItems.WARDEN_HEART.get());
                        pOutput.accept(NoezItems.MOONSILVER_INGOT.get());
                        pOutput.accept(NoezItems.ROSEGOLD_INGOT.get());
                        pOutput.accept(NoezItems.INFUSED_COPPER.get());
                        pOutput.accept(NoezItems.OBSIDIAN_SHARD.get());
                        pOutput.accept(NoezItems.IMPERIALITE.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> NOEZ_TAB_FOOD = CREATIVE_MODE_TABS.register("noez_tab_food",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NoezItems.FRUIT_OF_DECEPTION.get()))
                    .title(Component.translatable("creativetab.noez_tab_food"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(NoezItems.FRUIT_OF_DECEPTION.get());
                        pOutput.accept(Items.ENCHANTED_GOLDEN_APPLE);
                        pOutput.accept(NoezItems.FRUIT_OF_MADNESS.get());
                        pOutput.accept(NoezItems.FRUIT_OF_ENLIGHTENMENT.get());
                        pOutput.accept(NoezItems.MANA_POTION.get());
                        pOutput.accept(NoezItems.VITAL_WRAP.get());
                        pOutput.accept(NoezItems.SOUL_PEARL.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> NOEZ_TAB_TREASURES = CREATIVE_MODE_TABS.register("noez_tab_treasures",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NoezItems.COMMON_KEY.get()))
                    .title(Component.translatable("creativetab.noez_tab_treasures"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(NoezBlocks.COMMON_VAULT.get());
                        pOutput.accept(NoezItems.COMMON_KEY.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> NOEZ_TAB_COMBAT = CREATIVE_MODE_TABS.register("noez_tab_combat",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NoezItems.WINDBREAKER.get()))
                    .title(Component.translatable("creativetab.noez_tab_combat"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(NoezItems.DRUVIS_STAFF.get());
                        pOutput.accept(NoezItems.CRIMSON_SOUL_EATER.get());
                        pOutput.accept(NoezItems.IMPERIALITE_SWORD.get());
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

    public static final RegistryObject<CreativeModeTab> NOEZ_TAB_ARMOR = CREATIVE_MODE_TABS.register("noez_tab_armor",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NoezItems.KITSUNE_MASK.get()))
                    .title(Component.translatable("creativetab.noez_tab_armor"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(NoezItems.ECHO_HELMET.get());
                        pOutput.accept(NoezItems.ECHO_CHESTPLATE.get());
                        pOutput.accept(NoezItems.ECHO_LEGGINGS.get());
                        pOutput.accept(NoezItems.ECHO_BOOTS.get());
                        pOutput.accept(NoezItems.ECHO_ELYTRA.get());
                        pOutput.accept(NoezItems.KITSUNE_MASK.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> NOEZ_TAB_TOOL = CREATIVE_MODE_TABS.register("noez_tab_tool",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(NoezItems.MOLTEN_PICKAXE.get()))
                    .title(Component.translatable("creativetab.noez_tab_tool"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(NoezItems.MOLTEN_PICKAXE.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
