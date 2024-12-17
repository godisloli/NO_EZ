package net.tiramisu.noez.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.tiramisu.noez.NOEZ;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class NoezTextureRenderer {
    @OnlyIn(Dist.CLIENT)
    public static void renderBloodSacrificeEffect(double x, double y, double z) {
        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        poseStack.translate(x, y, z);
        poseStack.scale(1.5F, 1.5F, 1.5F);
        mc.getTextureManager().bindForSetup(new ResourceLocation(NOEZ.MOD_ID, "textures/effect/blood_sacrifice_aoe.png"));
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(-1, 0, -1).uv(0, 0).endVertex();
        buffer.vertex(1, 0, -1).uv(1, 0).endVertex();
        buffer.vertex(1, 0, 1).uv(1, 1).endVertex();
        buffer.vertex(-1, 0, 1).uv(0, 1).endVertex();
        Tesselator.getInstance().end();
        RenderSystem.disableBlend();
        poseStack.popPose();
    }
}
