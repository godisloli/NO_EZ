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
        return new ResourceLocation(NOEZ.MOD_ID, "textures/entity/iridescent_arrow.png");
    }
}
