package net.tiramisu.noez.particles.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IridescentHeartParticles extends TextureSheetParticle {
    protected IridescentHeartParticles(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.friction = 0.75f;
        this.xd = pXSpeed;
        this.yd = pYSpeed;
        this.zd = pZSpeed;
        this.quadSize *= 0.55f;
        this.lifetime = 10 + level.random.nextInt(41);
        this.setSpriteFromAge(spriteSet);

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;

        this.xd += (level.random.nextDouble() * 0.1) - 0.05;
        this.yd += (level.random.nextDouble() * 0.1) - 0.05;
        this.zd += (level.random.nextDouble() * 0.1) - 0.05;
    }

    @Override
    public void tick() {
        super.tick();
        this.xd += (level.random.nextDouble() * 0.02) - 0.01;
        this.yd += (level.random.nextDouble() * 0.02) - 0.01;
        this.zd += (level.random.nextDouble() * 0.02) - 0.01;
        FadeOut();
    }

    private void FadeOut(){
        this.alpha = (-(1f / (float) lifetime) * age + 1) * (0.5f + level.random.nextFloat() * 0.5f);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet){
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz){
            return new IridescentHeartParticles(level, x, y, z, this.spriteSet, dx, dy, dz);
        }
    }
}
