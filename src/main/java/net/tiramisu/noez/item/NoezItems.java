package net.tiramisu.noez.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.item.armors.EchoArmor;
import net.tiramisu.noez.item.armors.EchoElytra;
import net.tiramisu.noez.item.armors.KitsuneMask;
import net.tiramisu.noez.item.arsenal.*;
import net.tiramisu.noez.item.consumables.ManaPotion;
import net.tiramisu.noez.item.consumables.SoulPearl;
import net.tiramisu.noez.item.consumables.VitalWrap;
import net.tiramisu.noez.item.tools.MoltenPickaxe;

public class NoezItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, NOEZ.MOD_ID);

    public static final RegistryObject<Item> SOUL = ITEMS.register("soul",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> SCULK_BONE = ITEMS.register("sculk_bone",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> WARDEN_HEART = ITEMS.register("warden_heart",
            () -> new Item(new Item.Properties().stacksTo(1).fireResistant()));

    public static final RegistryObject<Item> WAR_AXE = ITEMS.register("war_axe",
            () -> new AxeItem(NoezToolTier.HEAVY, 2, -3f, new Item.Properties().stacksTo(1).durability(122)));

    public static final RegistryObject<Item> RELOCATOR = ITEMS.register("relocator",
            ()-> new Relocator(new Item.Properties().stacksTo(1).durability(25)));

    public static final RegistryObject<Item> NATURA_BLADE = ITEMS.register("natura_blade", NaturaBlade::new);

    public static final RegistryObject<Item> INFUSED_COPPER = ITEMS.register("infused_copper",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> OBSIDIAN_SHARD = ITEMS.register("obsidian_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> ROSEGOLD_INGOT = ITEMS.register("rosegold_ingot",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> IMPERIALITE = ITEMS.register("imperialite",
            () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));

    public static final RegistryObject<Item> MOLTEN_PICKAXE = ITEMS.register("molten_pickaxe",
            () -> new MoltenPickaxe(NoezToolTier.MEDIUM, 0, -2.4f, new Item.Properties().stacksTo(1).durability(310).fireResistant()));

    public static final RegistryObject<Item> OBSIDIAN_BLADE = ITEMS.register("obsidian_blade",
            () -> new ObsidianBlade(NoezToolTier.HEAVY, 4, -3.5f, new Item.Properties().stacksTo(1).durability(250)));

    public static final RegistryObject<Item> DARK_DEVASTATION = ITEMS.register("dark_devastation",
            () -> new DarkDevastation(NoezToolTier.HEAVY, 3, -3.2f, new Item.Properties().stacksTo(1).durability(212)));

    public static final RegistryObject<Item> HELLFIRE_SWORD = ITEMS.register("hellfire_sword",
            () -> new HellfireSword(NoezToolTier.MEDIUM,2,-2.4f, new Item.Properties().stacksTo(1).durability(152)));

    public static final RegistryObject<Item> CRIMSON_SOUL_EATER = ITEMS.register("crimson_soul_eater",
            () -> new CrimsonSoulEater(NoezToolTier.MEDIUM, 3, -2.4f, new Item.Properties().stacksTo(1).durability(152).fireResistant()));

    public static final RegistryObject<Item> BLOOD_SACRIFICE = ITEMS.register("blood_sacrifice",
            () -> new BloodSacrifice(NoezToolTier.MEDIUM,5,-2.4f, new Item.Properties().stacksTo(1).durability(164)));

    public static final RegistryObject<Item> FROSTBANE_SWORD = ITEMS.register("frostbane_sword",
            () -> new FrostbaneSword(NoezToolTier.MEDIUM,2,-2.4f, new Item.Properties().stacksTo(1).durability(152)));

    public static final RegistryObject<Item> WINDBREAKER = ITEMS.register("windbreaker",
            () -> new WindBreaker(NoezToolTier.MEDIUM,1,-3.2f,new Item.Properties().stacksTo(1).durability(142)));

    public static final RegistryObject<Item> BAMBOO_SWORD = ITEMS.register("bamboo_sword",
            () -> new BambooSword(NoezToolTier.LIGHT, 1, -2f, new Item.Properties().stacksTo(1).durability(58)));

    public static final RegistryObject<Item> MOONSILVER_INGOT = ITEMS.register("moonsilver_ingot",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> MOONSILVER_SWORD = ITEMS.register("moonsilver_sword",
            () -> new MoonsilverSword(NoezToolTier.MEDIUM, 2, -2.4f, new Item.Properties().stacksTo(1).durability(121)));

    public static final RegistryObject<Item> IMPERIALITE_SWORD = ITEMS.register("imperialite_sword", ImperialiteSword::new);

    public static final RegistryObject<Item> IRIDESCENT_BOW = ITEMS.register("iridescent_bow",
            () -> new IridescentBow(new Item.Properties().stacksTo(1).durability(112)));

    public static final RegistryObject<Item> VOID_STALKER = ITEMS.register("void_stalker",
            () -> new VoidStalker(new Item.Properties().stacksTo(1).durability(98)));

    public static final RegistryObject<Item> MECHANICAL_CROSSBOW = ITEMS.register("mechanical_crossbow",
            () -> new MechanicalCrossbow(new Item.Properties().stacksTo(1).durability(118)));

    public static final RegistryObject<Item> FRUIT_OF_DECEPTION = ITEMS.register("fruit_of_deception",
            () -> new NoezFoods(new Item.Properties().food(NoezFoods.FRUIT_OF_DECEPTION).stacksTo(64)).setFOIL(true));

    public static final RegistryObject<Item> FRUIT_OF_ENLIGHTENMENT = ITEMS.register("fruit_of_enlightenment",
            () -> new NoezFoods(new Item.Properties().food(NoezFoods.FRUIT_OF_ENLIGHTENMENT).stacksTo(64)).setFOIL(true));

    public static final RegistryObject<Item> FRUIT_OF_MADNESS = ITEMS.register("fruit_of_madness",
            () -> new NoezFoods(new Item.Properties().food(NoezFoods.FRUIT_OF_MADNESS).stacksTo(64)).setFOIL(true));

    public static final RegistryObject<Item> MANA_POTION = ITEMS.register("mana_potion",
            () -> new ManaPotion(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> DRUVIS_STAFF = ITEMS.register("druvis_staff", DruvisStaff::new);

    public static final RegistryObject<Item> SOUL_PEARL = ITEMS.register("soul_pearl",
            () -> new SoulPearl(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> VITAL_WRAP = ITEMS.register("vital_wrap",
            () -> new VitalWrap(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> ECHO_HELMET = ITEMS.register("echo_helmet",
            () -> new EchoArmor(NoezArmorTier.ECHO, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> ECHO_CHESTPLATE = ITEMS.register("echo_chestplate",
            () -> new EchoArmor(NoezArmorTier.ECHO, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<Item> ECHO_LEGGINGS = ITEMS.register("echo_leggings",
            () -> new EchoArmor(NoezArmorTier.ECHO, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<Item> ECHO_BOOTS = ITEMS.register("echo_boots",
            () -> new EchoArmor(NoezArmorTier.ECHO, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> ECHO_ELYTRA = ITEMS.register("echo_elytra",
            () -> new EchoElytra(new Item.Properties().stacksTo(1).durability(2413).fireResistant()));

    public static final RegistryObject<Item> KITSUNE_MASK = ITEMS.register("kitsune_mask",
            () -> new KitsuneMask(NoezArmorTier.KITSUNE, ArmorItem.Type.HELMET, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> COMMON_KEY = ITEMS.register("common_key",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
