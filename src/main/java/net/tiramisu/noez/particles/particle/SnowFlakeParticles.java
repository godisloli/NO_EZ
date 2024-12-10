package net.tiramisu.noez.particles.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SnowFlakeParticles extends TextureSheetParticle {

    protected SnowFlakeParticles(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.friction = 0.98f;
        this.xd = (level.random.nextDouble() * 0.02) - 0.01;
        this.yd = -0.02;
        this.zd = (level.random.nextDouble() * 0.02) - 0.01;
        this.quadSize *= 0.25f + level.random.nextFloat() * 0.3f;
        this.lifetime = 20 + level.random.nextInt(40);
        this.setSpriteFromAge(spriteSet);
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    @Override
    public void tick() {
        super.tick();
        this.yd -= 0.001;
        this.xd += (level.random.nextDouble() * 0.002) - 0.001;
        this.zd += (level.random.nextDouble() * 0.002) - 0.001;
        this.xd *= this.friction;
        this.yd *= this.friction;
        this.zd *= this.friction;
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
            return new SnowFlakeParticles(level, x, y, z, this.spriteSet, dx, dy, dz);
        }
    }
}
