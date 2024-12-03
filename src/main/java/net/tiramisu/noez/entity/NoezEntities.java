package net.tiramisu.noez.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.entity.arrows.IridescentArrow;


public class NoezEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, NOEZ.MOD_ID);

    public static final RegistryObject<EntityType<IridescentArrow>> IRIDESCENT_ARROW = ENTITY_TYPES.register("iridescent_arrow",
            () -> EntityType.Builder.<IridescentArrow>of(IridescentArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(8)
                    .updateInterval(10)
                    .build(NOEZ.MOD_ID + ":iridescent_arrow"));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
