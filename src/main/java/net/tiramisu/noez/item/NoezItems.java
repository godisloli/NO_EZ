package net.tiramisu.noez.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.item.weaponstools.NaturaBlade;
import net.tiramisu.noez.item.weaponstools.Relocator;

public class NoezItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, NOEZ.MOD_ID);

    public static final RegistryObject<Item> SOUL = ITEMS.register("soul",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> RELOCATOR = ITEMS.register("relocator",
            ()-> new Relocator(new Item.Properties().stacksTo(1).durability(25)));

    public static final RegistryObject<SwordItem> NATURABLADE = ITEMS.register("naturablade",
            ()-> new NaturaBlade(Tiers.NETHERITE,3,-2.4F, new Item.Properties().durability(2500)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
