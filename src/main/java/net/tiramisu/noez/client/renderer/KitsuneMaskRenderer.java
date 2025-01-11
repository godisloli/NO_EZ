package net.tiramisu.noez.client.renderer;

import net.tiramisu.noez.client.models.KitsuneMaskModel;
import net.tiramisu.noez.item.armors.KitsuneMask;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class KitsuneMaskRenderer extends GeoArmorRenderer<KitsuneMask> {
    public KitsuneMaskRenderer() {
        super(new KitsuneMaskModel());
    }
}
