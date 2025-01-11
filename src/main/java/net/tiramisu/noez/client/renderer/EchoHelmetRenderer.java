package net.tiramisu.noez.client.renderer;

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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.client.models.EchoHelmetModel;
import net.tiramisu.noez.item.NoezItems;

public class EchoHelmetRenderer<E extends LivingEntity, M extends HumanoidModel<E>> extends RenderLayer<E, M> {
    public static final ModelLayerLocation MODEL = new ModelLayerLocation(new ResourceLocation(NOEZ.MOD_ID, "echo"), "3");
    private static final ResourceLocation TEXTURE_LAYER = new ResourceLocation(NOEZ.MOD_ID, "textures/models/armor/echo_layer_3.png");

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
            this.getParentModel().getHead().translateAndRotate(poseStack);
            EchoHelmetModel<E> helmetModel = new EchoHelmetModel<>(this.modelSet.bakeLayer(MODEL));
            helmetModel.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(TEXTURE_LAYER)), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
    }
}
