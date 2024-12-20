package net.tiramisu.noez.entity.nonarrows;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.tiramisu.noez.NOEZ;

public class NoezNonArrowRenderer extends EntityRenderer<GrassSpellShot> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(NOEZ.MOD_ID, "textures/entity/grass_spell_shot.png");

    public NoezNonArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(GrassSpellShot entity) {
        return TEXTURE;
    }

    @Override
    public void render(GrassSpellShot entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
