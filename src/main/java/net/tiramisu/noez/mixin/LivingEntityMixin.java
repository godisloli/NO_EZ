package net.tiramisu.noez.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.tiramisu.noez.attribute.NoezAttributes;
import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.event.global.LineOfSight;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.item.LifeStealable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    private float preDamageHealth;

    public LivingEntityMixin(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void reduceIFrames(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getDirectEntity() instanceof AbstractArrow arrow) {
            if (arrow.getOwner() instanceof LivingEntity) {
                this.invulnerableTime = 1;
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "hasLineOfSight", cancellable = true)
    void isLookingAtMe(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Player)
            return;
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        if (entity instanceof LivingEntity) {
            if (!LineOfSight.isLookingAtYou(livingEntity, entity)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "checkFallDamage", at = @At("HEAD"), cancellable = true)
    public void RelocatorNoFallDamage(double heightDifference, boolean onGround, BlockState blockState, BlockPos pos, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        Level world = entity.level();
        if (entity instanceof Player player) {
            long lastTeleportTime = player.getPersistentData().getLong("RelocatorTeleportTime");
            if (world.getGameTime() - lastTeleportTime <= 20) {
                player.fallDistance = 0;
                ci.cancel();
            }
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void reduceDamageOnLowDurability(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity target = (LivingEntity) (Object) this;
        if (pSource.getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();
            if (weapon.getDamageValue() >= weapon.getMaxDamage() - 1 && weapon.getMaxDamage() > 1) {
                target.hurt(target.damageSources().generic(), 1.0F);
                if (attacker instanceof Player){
                    attacker.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(),
                            SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                cir.cancel();
            }
        }
    }

    @Inject(method = "getArmorValue", at = @At("HEAD"), cancellable = true)
    private void modifyArmorValue(CallbackInfoReturnable<Integer> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        int totalReducedArmor = 0;
        for (ItemStack armorPiece : livingEntity.getArmorSlots()) {
            if (armorPiece.getItem() instanceof ArmorItem armorItem &&
                    armorPiece.getDamageValue() >= armorPiece.getMaxDamage() - 1) {
                totalReducedArmor += armorItem.getDefense();
            }
        }

        if (livingEntity.hasEffect(NoezEffects.CORRUPTION.get())) {
            int reducedArmor;
            switch (livingEntity.getEffect(NoezEffects.CORRUPTION.get()).getAmplifier()) {
                case 0 -> reducedArmor = 3;
                case 1 -> reducedArmor = 5;
                case 2 -> reducedArmor = 7;
                case 3 -> reducedArmor = 9;
                case 4 -> reducedArmor = 12;
                default -> reducedArmor = 15;
            }
            totalReducedArmor += reducedArmor;
        }

        int baseArmorValue = 0;
        for (ItemStack armorPiece : livingEntity.getArmorSlots()) {
            if (armorPiece.getItem() instanceof ArmorItem armorItem) {
                baseArmorValue += armorItem.getDefense();
            }
        }
        int finalArmorValue = Math.max(0, baseArmorValue - totalReducedArmor);
        cir.setReturnValue(finalArmorValue);
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    private void damageCalculate(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        preDamageHealth = ((LivingEntity) (Object) this).getHealth();
    }

    @Inject(method = "hurt", at = @At("RETURN"))
    private void lifeStealLogic(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity target = (LivingEntity) (Object) this;
        if (source.getEntity() instanceof Player player) {
            float postDamageHealth = target.getHealth();
            float actualDamageDealt = preDamageHealth - postDamageHealth;
            if (actualDamageDealt > 0) {
                ItemStack itemStack = player.getMainHandItem();
                if (itemStack.getItem() instanceof LifeStealable lifeStealable) {
                    float lifeStealAmount = (float) (actualDamageDealt * lifeStealable.getLifeStealAmount());
                    player.heal(lifeStealAmount);
                }
            }
        }
    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"), cancellable = true)
    private static void addCustomAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        AttributeSupplier.Builder builder = cir.getReturnValue();
        builder.add(NoezAttributes.MAGIC_REDUCTION.get()).add(NoezAttributes.PROJECTILE_REDUCTION.get()).add(NoezAttributes.HEALTH_REGENERATION.get()).add(NoezAttributes.MANA_REGENERATION.get());
        cir.setReturnValue(builder);
    }
}