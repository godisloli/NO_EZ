package net.tiramisu.noez.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.item.NoezItems;

public class EchoElytraRenderer<E extends LivingEntity, M extends EntityModel<E>> extends ElytraLayer<E, M> {

    private static final ResourceLocation ECHO_ELYTRA_TEXTURE = new ResourceLocation(NOEZ.MOD_ID, "textures/entity/echo_elytra.png");
    private final ElytraModel<E> model;

    public EchoElytraRenderer(RenderLayerParent<E, M> pRenderer, EntityModelSet pModelSet) {
        super(pRenderer, pModelSet);
        this.model = new ElytraModel(pModelSet.bakeLayer(ModelLayers.ELYTRA));
    }

    public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, E livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemStack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (itemStack.getItem() == NoezItems.ECHO_ELYTRA.get()) {
            matrixStack.pushPose();
            matrixStack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(ECHO_ELYTRA_TEXTURE), false, itemStack.hasFoil());
            this.model.renderToBuffer(matrixStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        }
    }
}
