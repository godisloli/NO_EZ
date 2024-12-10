package net.tiramisu.noez.mixin;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.item.Critable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "attack", at = @At("HEAD"))
    private void NoezCritLogic(Entity target, CallbackInfo ci) {
        if (target instanceof LivingEntity targetEntity) {
            Player player = (Player) (Object) this;
            ItemStack mainHandStack = player.getMainHandItem();
            Item mainHandItem = mainHandStack.getItem();
            if (mainHandItem instanceof Critable) {
                double critChance = ((Critable) mainHandItem).getCritChance() + 0.15;
                double random = player.level().getRandom().nextDouble();
                if (random < critChance || ((Critable) mainHandItem).isAlwaysCrit() || ((LivingEntity) target).hasEffect(NoezEffects.FROSTBITE.get())) {
                    float baseDamage = (float) player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE).getValue();
                    float critDamage = baseDamage * (float)((Critable) mainHandItem).getCritDamageAmplifier();
                    targetEntity.hurt(player.damageSources().playerAttack(player), critDamage);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),SoundEvents.PLAYER_ATTACK_CRIT,SoundSource.PLAYERS,1.0f,1.0f);
                    Level level = player.level();
                    if(level instanceof ServerLevel serverLevel){
                        double offsetX = (player.level().getRandom().nextDouble() - 0.5) * target.getBbWidth();
                        double offsetY = player.level().getRandom().nextDouble() * target.getBbHeight();
                        double offsetZ = (player.level().getRandom().nextDouble() - 0.5) * target.getBbWidth();
                        for (int i = 0; i < 5; ++i) {
                            serverLevel.sendParticles(
                                    ParticleTypes.CRIT,
                                    targetEntity.getX() + offsetX,
                                    targetEntity.getY() + offsetY,
                                    targetEntity.getZ() + offsetZ,
                                    10,
                                    0.0, 0.0, 0.0,
                                    0.5
                            );
                        }
                    }
                }
            }
        }
    }
    @ModifyVariable(
            method = {"attack"},
            at = @At("STORE"),
            ordinal = 2
    )
    public boolean attack(boolean value) {
        return false;
    }
}
