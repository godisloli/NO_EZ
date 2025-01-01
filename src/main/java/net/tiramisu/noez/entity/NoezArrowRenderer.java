package net.tiramisu.noez.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.tiramisu.noez.NOEZ;

public class NoezArrowRenderer extends ArrowRenderer {

    public NoezArrowRenderer(EntityRendererProvider.Context context){
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Entity pEntity) {
        String textureName = Location(pEntity);
        return new ResourceLocation(NOEZ.MOD_ID, "textures/entity/" + textureName + ".png");
    }

    private String Location(Entity entity){
        if (entity.getType().toString().contains("iridescent_arrow")) {
            return "iridescent_arrow";
        } else if (entity.getType().toString().contains("root_projectile")) {
            return "root_projectile";
        } else if (entity.getType().toString().contains("void_arrow")) {
            return "void_arrow";
        }
        return "default_arrow";
    }
}
