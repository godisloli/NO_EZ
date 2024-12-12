package net.tiramisu.noez.particles.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WindBlowParticles extends TextureSheetParticle {

    protected WindBlowParticles(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.xd = (level.random.nextDouble() - 0.5) * 0.5;
        this.yd = (level.random.nextDouble() - 0.5) * 0.2;
        this.zd = (level.random.nextDouble() - 0.5) * 0.5;
        this.quadSize *= 0.5f + level.random.nextFloat() * 2f;
        this.lifetime = 5 * 2;
        this.setSpriteFromAge(spriteSet);
        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age >= this.lifetime) {
            this.remove();
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
            return new WindBlowParticles(level, x, y, z, this.spriteSet, dx, dy, dz);
        }
    }
}
