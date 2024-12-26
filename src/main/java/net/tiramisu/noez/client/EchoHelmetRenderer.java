package net.tiramisu.noez.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.tiramisu.noez.item.NoezItems;

public class EchoHelmetRenderer<E extends LivingEntity, M extends HumanoidModel<E>> extends RenderLayer<E, M> {
    private static final ResourceLocation TEXTURE_LAYER_1 = new ResourceLocation("noez", "textures/models/armor/echo_layer_1.png");
    private static final ResourceLocation TEXTURE_LAYER_3 = new ResourceLocation("noez", "textures/models/armor/echo_layer3.png");
    public static final ModelLayerLocation MODEL = new ModelLayerLocation(new ResourceLocation("noez", "echo_helmet"), "main");

    private final EntityModelSet modelSet;

    public EchoHelmetRenderer(RenderLayerParent<E, M> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.modelSet = modelSet;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, E entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (stack.getItem() == NoezItems.ECHO_HELMET.get()) {
            poseStack.pushPose();
            poseStack.scale(1.0F, 1.0F, 1.0F);
            EchoHelmetModel<E> helmetModel = new EchoHelmetModel<>(this.modelSet.bakeLayer(MODEL));
            helmetModel.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(TEXTURE_LAYER_1)), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            helmetModel.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(TEXTURE_LAYER_3)), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
    }
}
