package net.tiramisu.noez.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.HashMap;
import java.util.Map;

public class VillageDetect {

    private static final int VILLAGE_RADIUS_CHUNKS = 8;
    private static final int VILLAGE_RADIUS_BLOCKS = VILLAGE_RADIUS_CHUNKS * 16;
    private static final Map<SectionPos, Boolean> villageCache = new HashMap<>();

    public static boolean isInVillage(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return false;
        }
        SectionPos sectionPos = SectionPos.of(pos);

        if (villageCache.containsKey(sectionPos)) {
            return villageCache.get(sectionPos);
        }

        boolean isVillage = false;
        for (int dx = -VILLAGE_RADIUS_BLOCKS; dx <= VILLAGE_RADIUS_BLOCKS; dx += 16) {
            for (int dz = -VILLAGE_RADIUS_BLOCKS; dz <= VILLAGE_RADIUS_BLOCKS; dz += 16) {
                for (int dy = -VILLAGE_RADIUS_BLOCKS; dy <= VILLAGE_RADIUS_BLOCKS; dy += 16) {
                    SectionPos nearbySection = sectionPos.offset(dx / 16, dy / 16, dz / 16);
                    LevelChunk chunk = serverLevel.getChunk(nearbySection.x(), nearbySection.z());

                    if (isVillageSubchunk(chunk, nearbySection)) {
                        isVillage = true;
                        break;
                    }
                }
            }
        }

        villageCache.put(sectionPos, isVillage);
        return isVillage;
    }



    private static boolean isVillageSubchunk(LevelChunk chunk, SectionPos sectionPos) {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    BlockPos blockPos = sectionPos.origin().offset(x, y, z);
                    if (isVillageBlock(chunk, blockPos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isVillageBlock(ChunkAccess chunk, BlockPos blockPos) {
        return chunk.getBlockState(blockPos).is(BlockTags.BEDS) ||
                chunk.getBlockState(blockPos).is(Blocks.BELL) ||
                chunk.getBlockState(blockPos).is(Blocks.SMITHING_TABLE) ||
                chunk.getBlockState(blockPos).is(Blocks.LOOM) ||
                chunk.getBlockState(blockPos).is(Blocks.CARTOGRAPHY_TABLE) ||
                chunk.getBlockState(blockPos).is(Blocks.BARREL) ||
                chunk.getBlockState(blockPos).is(Blocks.BLAST_FURNACE) ||
                chunk.getBlockState(blockPos).is(Blocks.COMPOSTER) ||
                chunk.getBlockState(blockPos).is(Blocks.LECTERN);
    }
}
