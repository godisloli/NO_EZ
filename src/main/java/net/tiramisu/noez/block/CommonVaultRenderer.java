package net.tiramisu.noez.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.tiramisu.noez.block.vaults.CommonVaultBlockEntity;

public class CommonVaultRenderer implements BlockEntityRenderer<CommonVaultBlockEntity> {

    public CommonVaultRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(CommonVaultBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack storedItem = blockEntity.getStoredItem();

        if (!storedItem.isEmpty()) {
            poseStack.pushPose();

            poseStack.translate(0.5, 0.25, 0.5);
            poseStack.scale(1f, 1f, 1f);
            long gameTime = blockEntity.getLevel().getGameTime();
            float rotation = (gameTime + partialTick) % 360;

            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

            Minecraft minecraft = Minecraft.getInstance();
            ItemRenderer itemRenderer = minecraft.getItemRenderer();

            itemRenderer.renderStatic(
                    storedItem,
                    ItemDisplayContext.GROUND,
                    packedLight,
                    packedOverlay,
                    poseStack,
                    bufferSource,
                    blockEntity.getLevel(),
                    0
            );

            poseStack.popPose();
        }
    }
}

