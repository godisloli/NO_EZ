package net.tiramisu.noez.client;

import net.minecraft.resources.ResourceLocation;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.item.armors.KitsuneMask;
import software.bernie.geckolib.model.GeoModel;

public class KitsuneMaskModel extends GeoModel<KitsuneMask> {
    @Override
    public ResourceLocation getModelResource(KitsuneMask kitsuneMask) {
        return new ResourceLocation(NOEZ.MOD_ID, "geo/kitsune_mask.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KitsuneMask kitsuneMask) {
        return new ResourceLocation(NOEZ.MOD_ID, "textures/models/armor/kitsune_mask.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KitsuneMask kitsuneMask) {
        return new ResourceLocation(NOEZ.MOD_ID, "animations/kitsune_mask.animation.json");
    }
}
