package net.tiramisu.noez.client;

import net.tiramisu.noez.item.armors.KitsuneMask;
import net.tiramisu.noez.item.armors.TwilightWitchHat;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class TwilightWitchHatRenderer extends GeoArmorRenderer<TwilightWitchHat> {
    public TwilightWitchHatRenderer() {
        super(new TwilightWitchHatModel());
    }
}
