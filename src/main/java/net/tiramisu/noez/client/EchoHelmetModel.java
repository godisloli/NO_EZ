package net.tiramisu.noez.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class EchoHelmetModel<E extends LivingEntity> extends EntityModel<E> {
    private final ModelPart head;
    private final ModelPart horn;
    public static final ModelLayerLocation MODEL = new ModelLayerLocation(new ResourceLocation("noez", "echo_helmet"), "main");

    public EchoHelmetModel(ModelPart root) {
        this.head = root.getChild("head");
        this.horn = root.getChild("horn");
    }

    public static LayerDefinition createBodyModel() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition parts = mesh.getRoot();
        PartDefinition head = parts.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        PartDefinition horn = parts.addOrReplaceChild("horn", CubeListBuilder.create().texOffs(16, 0)
                .addBox(-1.0F, -10.0F, -2.0F, 2.0F, 3.0F, 2.0F), PartPose.rotation(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.horn.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
