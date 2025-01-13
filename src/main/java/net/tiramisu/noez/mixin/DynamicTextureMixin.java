package net.tiramisu.noez.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.tiramisu.noez.util.Darkness;
import net.tiramisu.noez.util.TextureAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DynamicTexture.class)
public class DynamicTextureMixin implements TextureAccess {
    @Shadow
    NativeImage pixels;

    private boolean enableHook = false;

    public DynamicTextureMixin() {
    }

    @Inject(
            method = {"upload"},
            at = @At("HEAD")
    )
    private void onUpload(CallbackInfo ci) {
        if (this.enableHook && Darkness.enabled && this.pixels != null) {
            NativeImage img = this.pixels;
            for (int x = 0; x < 16; ++x) {
                for (int y = 0; y < 16; ++y) {
                    int color = img.getPixelRGBA(x, y);
                    int darkenedColor = Darkness.darken(color, x, y);
                    img.setPixelRGBA(x, y, darkenedColor);
                }
            }
        }
    }

    public void darkness_enableUploadHook() {
        this.enableHook = true;
    }
}
