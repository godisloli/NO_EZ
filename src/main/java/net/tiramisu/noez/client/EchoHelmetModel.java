package net.tiramisu.noez.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class EchoHelmetModel<E extends LivingEntity> extends EntityModel<E> implements HeadedModel {
    private final ModelPart head;

    public EchoHelmetModel(ModelPart head) {
        this.head = head;
    }

    public static LayerDefinition createBodyModel() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition parts = mesh.getRoot();

        parts.addOrReplaceChild("root",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-11.95F, -13.25F, 0.0F, 8.0F, 10.0F, 0.05F, new CubeDeformation(0.0F))
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(3.95F, -13.25F, 0.0F, 8.0F, 10.0F, 0.05F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -0.5F, 0.0F)
        );

        parts.addOrReplaceChild("horn_left",
                CubeListBuilder.create()
                        .texOffs(8, 0)
                        .mirror()
                        .addBox(-11.95F, -13.25F, 0.0F, 8.0F, 10.0F, 0.05F, new CubeDeformation(0.0F))
                        .texOffs(0, 0),
                PartPose.offset(0.0F, -0.5F, 0.0F)
        );

        parts.addOrReplaceChild("horn_right",
                CubeListBuilder.create()
                        .texOffs(8, 0)
                        .addBox(3.95F, -13.25F, 0.0F, 8.0F, 10.0F, 0.05F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -0.5F, 0.0F)
        );

        return LayerDefinition.create(mesh, 16, 16);
    }

    @Override
    public void setupAnim(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        netHeadYaw = Mth.clamp(netHeadYaw, -30.0F, 30.0F);
        headPitch = Mth.clamp(headPitch, -25.0F, 45.0F);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getHead() {
        return this.head;
    }
}
