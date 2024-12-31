package net.tiramisu.noez.attribute;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tiramisu.noez.NOEZ;

public class NoezAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, NOEZ.MOD_ID);

    public static final RegistryObject<Attribute> PROJECTILE_REDUCTION =
            ATTRIBUTES.register("projectile_reduction", () ->
                    new RangedAttribute(
                            "attribute.name.generic.projectile_reduction",
                            0.0,
                            0.0,
                            90.0
                    ).setSyncable(true));

    public static final RegistryObject<Attribute> MAGIC_REDUCTION =
            ATTRIBUTES.register("magic_reduction", () ->
                    new RangedAttribute(
                            "attribute.name.generic.magic_reduction",
                            0.0,
                            0.0,
                            90.0
                    ).setSyncable(true));

    public static final RegistryObject<Attribute> HEALTH_REGENERATION =
            ATTRIBUTES.register("health_regeneration", () ->
                    new RangedAttribute(
                            "attribute.name.generic.health_regeneration",
                            0.0,
                            0.0,
                            999.0
                    ).setSyncable(true));

    public static final RegistryObject<Attribute> MANA_REGENERATION =
            ATTRIBUTES.register("mana_regeneration", () ->
                    new RangedAttribute(
                            "attribute.name.generic.mana_regeneration",
                            0.0,
                            0.0,
                            999.0
                    ).setSyncable(true));

    public static final RegistryObject<Attribute> CRIT_CHANCE =
            ATTRIBUTES.register("crit_chance", () ->
                    new RangedAttribute(
                            "attribute.name.generic.crit_chance",
                            0.0,
                            0.0,
                            100.0
                    ).setSyncable(true));

    public static final RegistryObject<Attribute> CRIT_DAMAGE =
            ATTRIBUTES.register("crit_damage", () ->
                    new RangedAttribute(
                            "attribute.name.generic.crit_damage",
                            0.0,
                            0.0,
                            999.0
                    ).setSyncable(true));

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}
