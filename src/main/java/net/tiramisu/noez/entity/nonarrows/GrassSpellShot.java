package net.tiramisu.noez.entity.nonarrows;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.item.SpellCaster;

public class GrassSpellShot extends ThrowableProjectile {
    private static final int DURATION = 4;
    private static final double ROOT_CHANCE = 0.2;
    private static final float AMPLIFIER = 1.5f;
    private static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(GrassSpellShot.class, EntityDataSerializers.INT);

    public GrassSpellShot(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public GrassSpellShot(EntityType<? extends ThrowableProjectile> pEntityType, double pX, double pY, double pZ, net.minecraft.world.level.Level pLevel) {
        super(pEntityType, pX, pY, pZ, pLevel);
    }

    public GrassSpellShot(EntityType<? extends ThrowableProjectile> pEntityType, LivingEntity pShooter, net.minecraft.world.level.Level pLevel) {
        super(pEntityType, pShooter, pLevel);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (this.getOwner() instanceof LivingEntity livingEntity) {
            ItemStack itemStack = livingEntity.getMainHandItem();
            if (itemStack.getItem() instanceof SpellCaster spellCaster){
                if (!this.level().isClientSide()) {
                    Entity target = result.getEntity();
                    if (target instanceof LivingEntity livingTarget) {
                        livingTarget.hurt(this.getOwner().damageSources().magic(), livingTarget.hurt(this.getOwner().damageSources().magic(), (float) spellCaster.getMagicDamage()) ? (float) spellCaster.getMagicDamage() : (float) spellCaster.getMagicDamage() * AMPLIFIER) ;
                        double random = this.getOwner().level().getRandom().nextDouble();
                        boolean flag = random < ROOT_CHANCE;
                        if (flag && !livingTarget.hasEffect(NoezEffects.ROOT.get())) {
                            livingTarget.addEffect(new MobEffectInstance(NoezEffects.ROOT.get(), DURATION * 20, 0));
                        }
                        this.playSound(SoundEvents.GRASS_BREAK, 1.0F, 1.0F);
                        this.discard();
                    }
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(LIFESPAN, 0);
    }

    public void setLifespan(int ticks) {
        this.entityData.set(LIFESPAN, ticks);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        spawnParticles();
        if (this.tickCount >= this.entityData.get(LIFESPAN)) {
            this.discard();
        }
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity * 0.5F, inaccuracy);
    }

    private void spawnParticles() {
        if (!this.level().isClientSide) return;
        for (int i = 0; i < 3; i++) {
            this.level().addParticle(ParticleTypes.CHERRY_LEAVES, this.getX(), this.getY(), this.getZ(),
                    this.getDeltaMovement().x * 0.2, this.getDeltaMovement().y * 0.2, this.getDeltaMovement().z * 0.2);
        }
    }
}
