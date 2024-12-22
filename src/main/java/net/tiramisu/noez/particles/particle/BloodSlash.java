package net.tiramisu.noez.particles.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

public class BloodSlash extends TextureSheetParticle {

    private SpriteSet spriteSet;

    protected BloodSlash(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.age = 0;
        this.lifetime = 13;
        this.quadSize = 2.5F;
        this.spriteSet = spriteSet;
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        float x = (float) (Mth.lerp(partialTicks, this.xo, this.x) - camera.getPosition().x());
        float y = (float) (Mth.lerp(partialTicks, this.yo, this.y) - camera.getPosition().y());
        float z = (float) (Mth.lerp(partialTicks, this.zo, this.z) - camera.getPosition().z());
        float scale = this.getQuadSize(partialTicks);

        Vector3f[] corners = new Vector3f[] {
                new Vector3f(-scale, 0, -scale),
                new Vector3f(-scale, 0, scale),
                new Vector3f(scale, 0, scale),
                new Vector3f(scale, 0, -scale)
        };

        int light = this.getLightColor(partialTicks);
        this.setSpriteFromAge(this.spriteSet);
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();

        for (int i = 0; i < 4; ++i) {
            Vector3f corner = corners[i];
            vertexConsumer.vertex(x + corner.x(), y + corner.y(), z + corner.z())
                    .uv(i < 2 ? u0 : u1, (i == 0 || i == 3) ? v0 : v1)
                    .color(this.rCol, this.gCol, this.bCol, this.alpha)
                    .uv2(light)
                    .endVertex();
        }

        for (int i = 0; i < 4; ++i) {
            Vector3f corner = corners[3 - i]; // Reverse corner order for correct winding
            vertexConsumer.vertex(x + corner.x(), y + corner.y(), z + corner.z())
                    .uv(i < 2 ? u0 : u1, (i == 0 || i == 3) ? v0 : v1)
                    .color(this.rCol, this.gCol, this.bCol, this.alpha)
                    .uv2(light)
                    .endVertex();
        }
    }


    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new BloodSlash(level, x, y, z, this.spriteSet, dx, dy, dz);
        }
    }
}