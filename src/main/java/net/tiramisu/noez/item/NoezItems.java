package net.tiramisu.noez.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.item.arsenal.*;
import net.tiramisu.noez.item.consumables.ManaPotion;

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

    public static final RegistryObject<Item> DARK_DEVASTATION = ITEMS.register("dark_devastation",
            () -> new DarkDevastation(NoezToolTier.HEAVY, 3, -3.2f, new Item.Properties().stacksTo(1).durability(212)));

    public static final RegistryObject<Item> HELLFIRE_SWORD = ITEMS.register("hellfire_sword",
            () -> new HellfireSword(NoezToolTier.MEDIUM,2,-2.4f, new Item.Properties().stacksTo(1).durability(152)));

    public static final RegistryObject<Item> FROSTBANE_SWORD = ITEMS.register("frostbane_sword",
            () -> new FrostbaneSword(NoezToolTier.MEDIUM,2,-2.4f, new Item.Properties().stacksTo(1).durability(152)));

    public static final RegistryObject<Item> WINDBREAKER = ITEMS.register("windbreaker",
            () -> new WindBreaker(NoezToolTier.MEDIUM,1,-3.2f,new Item.Properties().stacksTo(1).durability(142)));

    public static final RegistryObject<Item> BAMBOO_SWORD = ITEMS.register("bamboo_sword",
            () -> new BambooSword(NoezToolTier.LIGHT, 1, -2f, new Item.Properties().stacksTo(1).durability(58)));

    public static final RegistryObject<Item> MOONSILVER_SWORD = ITEMS.register("moonsilver_sword",
            () -> new MoonsilverSword(NoezToolTier.MEDIUM, 2, -2.4f, new Item.Properties().stacksTo(1).durability(121)));

    public static final RegistryObject<Item> IRIDESCENT_BOW = ITEMS.register("iridescent_bow",
            () -> new IridescentBow(new Item.Properties().stacksTo(1).durability(112)));

    public static final RegistryObject<Item> FRUIT_OF_DECEPTION = ITEMS.register("fruit_of_deception",
            () -> new NoezFoods(new Item.Properties().food(NoezFoods.FRUIT_OF_DECEPTION).stacksTo(64)).setFOIL(true));

    public static final RegistryObject<Item> MANA_POTION = ITEMS.register("mana_potion",
            () -> new ManaPotion(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> DRUVIS_STAFF = ITEMS.register("druvis_staff", DruvisStaff::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
