package net.tiramisu.noez.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.tiramisu.noez.NOEZ;

public class NoezTags {
    public static class Blocks{
        public static TagKey<Block> BlockTag(String name){
            return BlockTags.create(new ResourceLocation(NOEZ.MOD_ID, name));
        }
    }

    public static class Items{
        public static final TagKey<Item> LIGHT_WEAPON_REPAIRABLE = ItemTag("light_weapon_repairable");
        public static final TagKey<Item> MEDIUM_WEAPON_REPAIRABLE = ItemTag("medium_weapon_repairable");
        public static final TagKey<Item> HEAVY_WEAPON_REPAIRABLE = ItemTag("heavy_weapon_repairable");
        public static final TagKey<Item> UNDEAD_SLAYING = ItemTag("undead_slaying");
        public static TagKey<Item> ItemTag(String name){
            return ItemTags.create(new ResourceLocation(NOEZ.MOD_ID, name));
        }
    }

    public static class Mobs {
        public static final TagKey<EntityType<?>>  NO_LINE_OF_SIGHT = MobTag("no_line_of_sight");
        public static final TagKey<EntityType<?>> CONSTRUCT_MOBS = MobTag("construct_mobs");
        public static final TagKey<EntityType<?>> RAIDER = MobTag("raider");

        public static TagKey<EntityType<?>> MobTag(String name) {
            return TagKey.create(BuiltInRegistries.ENTITY_TYPE.key(), new ResourceLocation(NOEZ.MOD_ID, name));
        }
    }
}
