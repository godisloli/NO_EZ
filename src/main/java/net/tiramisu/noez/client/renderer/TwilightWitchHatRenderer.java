package net.tiramisu.noez.client.renderer;

import net.tiramisu.noez.client.models.TwilightWitchHatModel;
import net.tiramisu.noez.item.armors.TwilightWitchHat;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class TwilightWitchHatRenderer extends GeoArmorRenderer<TwilightWitchHat> {
    public TwilightWitchHatRenderer() {
        super(new TwilightWitchHatModel());
    }
}
