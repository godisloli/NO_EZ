package net.tiramisu.noez.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.entity.arrows.IridescentArrow;
import net.tiramisu.noez.entity.arrows.RootProjectile;
import net.tiramisu.noez.entity.nonarrows.CrimsonScytheShot;
import net.tiramisu.noez.entity.nonarrows.GrassSpellShot;
import net.tiramisu.noez.entity.nonarrows.ThrownSoulPearl;


public class NoezEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, NOEZ.MOD_ID);

    public static final RegistryObject<EntityType<IridescentArrow>> IRIDESCENT_ARROW = ENTITY_TYPES.register("iridescent_arrow",
            () -> EntityType.Builder.<IridescentArrow>of(IridescentArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(8)
                    .updateInterval(10)
                    .build(NOEZ.MOD_ID + ":iridescent_arrow"));

    public static final RegistryObject<EntityType<RootProjectile>> ROOT_PROJECTILE = ENTITY_TYPES.register("root_projectile",
            () -> EntityType.Builder.<RootProjectile>of(RootProjectile::new, MobCategory.MISC)
                    .sized(0.2F, 0.2F)
                    .clientTrackingRange(8)
                    .updateInterval(10)
                    .build(NOEZ.MOD_ID + ":root_projectile"));

    public static final RegistryObject<EntityType<GrassSpellShot>> GRASS_SPELL_SHOT = ENTITY_TYPES.register("grass_spell_shot",
            () -> EntityType.Builder.<GrassSpellShot>of(GrassSpellShot::new, MobCategory.MISC)
                    .sized(0.18F, 0.18F)
                    .clientTrackingRange(8)
                    .updateInterval(10)
                    .build(NOEZ.MOD_ID + ":grass_spell_shot"));

    public static final RegistryObject<EntityType<CrimsonScytheShot>> CRIMSON_SCYTHE_SHOT = ENTITY_TYPES.register("crimson_scythe_shot",
            () -> EntityType.Builder.<CrimsonScytheShot>of(CrimsonScytheShot::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(8)
                    .updateInterval(10)
                    .build(NOEZ.MOD_ID + ":crimson_scythe_shot"));

    public static final RegistryObject<EntityType<ThrownSoulPearl>> SOUL_PEARL_ENTITY = ENTITY_TYPES.register("soul_pearl_entity",
            () -> EntityType.Builder.<ThrownSoulPearl>of(ThrownSoulPearl::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(8)
                    .updateInterval(10)
                    .build(NOEZ.MOD_ID +":soul_pearl_entity"));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
