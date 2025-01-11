package net.tiramisu.noez.client.models;

import net.minecraft.resources.ResourceLocation;
import net.tiramisu.noez.NOEZ;
import net.tiramisu.noez.item.armors.TwilightWitchHat;
import software.bernie.geckolib.model.GeoModel;

public class TwilightWitchHatModel extends GeoModel<TwilightWitchHat> {
    @Override
    public ResourceLocation getModelResource(TwilightWitchHat twilightWitchHat) {
        return new ResourceLocation(NOEZ.MOD_ID, "geo/twilight_witch_hat.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TwilightWitchHat twilightWitchHat) {
        return new ResourceLocation(NOEZ.MOD_ID, "textures/models/armor/twilight_witch_hat.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TwilightWitchHat twilightWitchHat) {
        return new ResourceLocation(NOEZ.MOD_ID, "animations/twilight_witch_hat.animation.json");
    }
}
