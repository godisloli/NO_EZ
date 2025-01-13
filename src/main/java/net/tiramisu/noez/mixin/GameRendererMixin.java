package net.tiramisu.noez.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.tiramisu.noez.util.Darkness;
import net.tiramisu.noez.util.LightmapAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    Minecraft minecraft;

    @Shadow
    private LightTexture lightTexture;

    @Inject(
            method = "renderLevel",
            at = @At("HEAD")
    )
    private void onRenderLevel(float tickDelta, long nanos, PoseStack matrixStack, CallbackInfo ci) {
        LightmapAccess lightmapAccess = (LightmapAccess) this.lightTexture;

        if (minecraft.level != null && lightmapAccess.darkness_isDirty()) {
            minecraft.getProfiler().push("lightTex");
            Darkness.updateLuminance(tickDelta, minecraft, (GameRenderer) (Object) this, lightmapAccess.darkness_prevFlicker());
            this.minecraft.getProfiler().pop();
        }
    }
}
