package net.tiramisu.noez.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class Darkness {
    static boolean darkOverworld = true;
    static boolean darkDefault = true;
    static boolean darkNether = true;
    static double darkNetherFogEffective = 0.5;
    static double darkNetherFogConfigured = 0.5;
    static boolean darkEnd = true;
    static double darkEndFogEffective = 0.0;
    static double darkEndFogConfigured = 0.0;
    static boolean darkSkyless = true;
    static boolean blockLightOnly = false;
    static boolean ignoreMoonPhase = false;
    public static boolean enabled = false;
    private static final float[][] LUMINANCE = new float[16][16];

    public Darkness() {
    }

    private static void computeConfigValues() {
        darkNetherFogEffective = darkNether ? darkNetherFogConfigured : 1.0;
        darkEndFogEffective = darkEnd ? darkEndFogConfigured : 1.0;
    }

    private static boolean isDark(Level world) {
        ResourceKey<Level> dimType = world.dimension();
        if (dimType == Level.OVERWORLD) {
            return darkOverworld;
        } else if (dimType == Level.NETHER) {
            return darkNether;
        } else if (dimType == Level.END) {
            return darkEnd;
        } else {
            return world.dimensionType().hasCeiling() ? darkDefault : darkSkyless;
        }
    }

    private static float skyFactor(Level world) {
        if (!blockLightOnly && isDark(world)) {
            if (world.dimensionType().hasSkyLight()) {
                float angle = world.getTimeOfDay(0.0F);
                if (angle > 0.25F && angle < 0.75F) {
                    float oldWeight = Math.max(0.0F, Math.abs(angle - 0.5F) - 0.2F) * 20.0F;
                    float moon = ignoreMoonPhase ? 0.0F : world.getMoonPhase();
                    return Mth.lerp(oldWeight * oldWeight * oldWeight, moon * moon, 1.0F);
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
            if (!isDark(world) || client.player.hasEffect(MobEffects.NIGHT_VISION) || (client.player.hasEffect(MobEffects.CONDUIT_POWER) && client.player.getEffect(MobEffects.CONDUIT_POWER).getDuration() > 0) || world.getSkyDarken() > 0) {
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
                float min = skyFactor * 0.05F;
                float rawAmbient = ambient * skyFactor;
                float minAmbient = rawAmbient * (1.0F - min) + min;
                float skyBase = LightTexture.getBrightness(dim, skyIndex) * minAmbient;
                min = 0.35F * skyFactor;
                float skyRed = skyBase * (rawAmbient * (1.0F - min) + min);
                float skyGreen = skyRed;
                float skyBlue = skyBase;

                if (worldRenderer.getDarkenWorldAmount(tickDelta) > 0.0F) {
                    float skyDarkness = worldRenderer.getDarkenWorldAmount(tickDelta);
                    skyRed = skyRed * (1.0F - skyDarkness) + skyRed * 0.7F * skyDarkness;
                    skyGreen = skyGreen * (1.0F - skyDarkness) + skyGreen * 0.6F * skyDarkness;
                    skyBlue = skyBase * (1.0F - skyDarkness) + skyBase * 0.6F * skyDarkness;
                }

                for (int blockIndex = 0; blockIndex < 16; ++blockIndex) {
                    float blockFactor = 1.0F;
                    if (!blockAmbient) {
                        blockFactor = 1.0F - (float) blockIndex / 15.0F;
                        blockFactor = 1.0F - blockFactor * blockFactor * blockFactor * blockFactor;
                    }

                    float blockBase = blockFactor * LightTexture.getBrightness(dim, blockIndex) * (prevFlicker * 0.1F + 1.5F);
                    min = 0.4F * blockFactor;
                    float blockGreen = blockBase * ((blockBase * (1.0F - min) + min) * (1.0F - min) + min);
                    float blockBlue = blockBase * (blockBase * blockBase * (1.0F - min) + min);
                    float red = skyRed + blockBase;
                    float green = skyGreen + blockGreen;
                    float blue = skyBlue + blockBlue;
                    float f = Math.max(skyFactor, blockFactor);
                    min = 0.03F * f;
                    red = red * (0.99F - min) + min;
                    green = green * (0.99F - min) + min;
                    blue = blue * (0.99F - min) + min;

                    if (world.dimension() == Level.END) {
                        red = skyFactor * 0.22F + blockBase * 0.75F;
                        green = skyFactor * 0.28F + blockGreen * 0.75F;
                        blue = skyFactor * 0.25F + blockBlue * 0.75F;
                    }

                    red = Mth.clamp(red, 0.0F, 1.0F);
                    green = Mth.clamp(green, 0.0F, 1.0F);
                    blue = Mth.clamp(blue, 0.0F, 1.0F);

                    float gamma = client.options.gamma().get().floatValue() * f;
                    red = red * (1.0F - gamma) + (1.0F - red) * gamma;
                    green = green * (1.0F - gamma) + (1.0F - green) * gamma;
                    blue = blue * (1.0F - gamma) + (1.0F - blue) * gamma;

                    LUMINANCE[blockIndex][skyIndex] = luminance(red, green, blue);
                }
            }
        }
    }

    static {
        computeConfigValues();
    }
}
