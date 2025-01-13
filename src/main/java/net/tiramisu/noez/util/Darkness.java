package net.tiramisu.noez.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class Darkness {
    static final boolean blockLightOnly = false;
    static final boolean darkOverworld = true;
    static final boolean darkDefault = true;
    static final boolean darkNether = true;
    static final boolean darkEnd = true;
    static final boolean darkSkyless = true;
    static final boolean ignoreMoonPhase = true;
    public static boolean enabled;
    private static final float[][] LUMINANCE = new float[16][16];

    private static boolean isDark(Level world) {
        ResourceKey<Level> dimType = world.dimension();
        if (dimType == Level.OVERWORLD) {
            return darkOverworld;
        } else if (dimType == Level.NETHER) {
            return darkNether;
        } else if (dimType == Level.END) {
            return darkEnd;
        } else {
            return world.dimensionType().hasSkyLight() ? darkDefault : darkSkyless;
        }
    }

    private static float skyFactor(Level world) {
        if (!blockLightOnly && isDark(world)) {
            if (world.dimensionType().hasSkyLight()) {
                float angle = world.getTimeOfDay(0.0F);
                if (angle > 0.25F && angle < 0.75F) {
                    float oldWeight = Math.max(0.0F, Math.abs(angle - 0.5F) - 0.2F) * 20.0F;
                    float moon = ignoreMoonPhase ? 0.0F : world.getMoonPhase();
                    return Mth.clamp(oldWeight * oldWeight * oldWeight, moon * moon, 1.0F);
                } else {
                    return 1.0F;
                }
            } else {
                return 0.0F;
            }
        } else {
            return 1.0F;
        }
    }

    public static int darken(int c, int blockIndex, int skyIndex) {
        float lTarget = LUMINANCE[blockIndex][skyIndex];
        float r = (float) (c & 255) / 255.0F;
        float g = (float) (c >> 8 & 255) / 255.0F;
        float b = (float) (c >> 16 & 255) / 255.0F;
        float l = luminance(r, g, b);
        float f = l > 0.0F ? Math.min(1.0F, lTarget / l) : 0.0F;
        return f == 1.0F ? c : -16777216 | Math.round(f * r * 255.0F) | Math.round(f * g * 255.0F) << 8 | Math.round(f * b * 255.0F) << 16;
    }

    public static float luminance(float r, float g, float b) {
        return r * 0.2126F + g * 0.7152F + b * 0.0722F;
    }

    public static void updateLuminance(float tickDelta, Minecraft client, GameRenderer worldRenderer, float prevFlicker) {
        ClientLevel world = client.level;
        if (world != null) {
            if (!isDark(world) || client.player.hasEffect(MobEffects.NIGHT_VISION) || (client.player.hasEffect(MobEffects.BLINDNESS) && client.player.getEffect(MobEffects.BLINDNESS).getDuration() > 0) || world.getSkyDarken() > 0) {
                enabled = false;
                return;
            }

            enabled = true;
            float dimSkyFactor = skyFactor(world);
            float ambient = world.getSkyDarken(1.0F);
            DimensionType dim = world.dimensionType();
            boolean blockAmbient = !isDark(world);

            for (int skyIndex = 0; skyIndex < 16; ++skyIndex) {
                float skyFactor = 1.0F - (float) skyIndex / 15.0F;
                skyFactor = 1.0F - skyFactor * skyFactor * skyFactor * skyFactor;
                skyFactor *= dimSkyFactor;

                for (int blockIndex = 0; blockIndex < 16; ++blockIndex) {
                    float blockFactor = blockAmbient ? 1.0F : (1.0F - (float) blockIndex / 15.0F);
                    blockFactor = 1.0F - blockFactor * blockFactor * blockFactor * blockFactor;

                    float red = skyFactor + blockFactor;
                    float green = skyFactor + blockFactor * 0.75F;
                    float blue = skyFactor + blockFactor * 0.6F;

                    LUMINANCE[blockIndex][skyIndex] = luminance(red, green, blue);
                }
            }
        }
    }
}
