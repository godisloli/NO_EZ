package net.tiramisu.noez.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.item.arsenal.*;
import net.tiramisu.noez.item.consumables.NoezFoods;

public class NoezItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, NOEZ.MOD_ID);

    public static final RegistryObject<Item> SOUL = ITEMS.register("soul",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> RELOCATOR = ITEMS.register("relocator",
            ()-> new Relocator(new Item.Properties().stacksTo(1).durability(25)));

    public static final RegistryObject<Item> NATURA_BLADE = ITEMS.register("natura_blade", NaturaBlade::new);

    public static final RegistryObject<Item> INFUSED_COPPER = ITEMS.register("infused_copper",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> OBSIDIAN_SHARD = ITEMS.register("obsidian_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> OBSIDIAN_BLADE = ITEMS.register("obsidian_blade",
            () -> new ObsidianBlade(NoezToolTier.HEAVY, 4, -3.5f, new Item.Properties().stacksTo(1).durability(250)));

    public static final RegistryObject<Item> HELLFIRE_SWORD = ITEMS.register("hellfire_sword",
            () -> new HellfireSword(NoezToolTier.MEDIUM,2,-2.4f, new Item.Properties().stacksTo(1).durability(152)));

    public static final RegistryObject<Item> FROSTBANE_SWORD = ITEMS.register("frostbane_sword",
            () -> new FrostbaneSword(NoezToolTier.MEDIUM,2,-2.4f, new Item.Properties().stacksTo(1).durability(152)));

    public static final RegistryObject<Item> IRIDESCENT_BOW = ITEMS.register("iridescent_bow",
            () -> new IridescentBow(new Item.Properties().stacksTo(1).durability(112)));

    public static final RegistryObject<Item> FRUIT_OF_DECEPTION = ITEMS.register("fruit_of_deception",
            () -> new NoezFoods(new Item.Properties().food(NoezFoods.FRUIT_OF_DECEPTION).stacksTo(64)).setFOIL(true));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
