package net.tiramisu.noez.mixin;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.util.NoezTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobMixin {
    @Inject(method = "serverAiStep", at = @At("HEAD"), cancellable = true)
    private void disablePathfindingWhileStunned(CallbackInfo ci) {
        Mob mob = (Mob) (Object) this;
        if (mob.hasEffect(NoezEffects.STUN.get())) {
            ci.cancel();
        }
    }

    private static final double MAX_RANGE = 10.0;
    private static final double CLOSE_RANGE = 4.0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void MobListen(CallbackInfo info) {
        Mob mob = (Mob) (Object) this;
        if (mob.getTarget() != null)
            return;
        if (mob.getType().is(NoezTags.Mobs.NO_LINE_OF_SIGHT))
            return;
        Level level = mob.level();
        for (Player player : level.getEntitiesOfClass(Player.class, mob.getBoundingBox().inflate(MAX_RANGE))) {
            if (player.isSpectator() || player.isDeadOrDying()) {
                continue;
            }
            double distance = mob.distanceTo(player);
            boolean isSneaking = player.isCrouching();
            if (!isSneaking) {
                turnMobToPlayer(mob, player);
                return;
            } else {
                double chance = (distance <= CLOSE_RANGE) ? 0.05 : 0.01;
                if (player.hasEffect(MobEffects.INVISIBILITY))
                    chance = 0.0025;
                if (mob.getRandom().nextDouble() < chance) {
                    turnMobToPlayer(mob, player);
                    return;
                }
            }
        }
    }

    private void turnMobToPlayer(Mob mob, Player player) {
        mob.getLookControl().setLookAt(player, 30.0F, 30.0F);
    }

    @Inject(method = "dropFromLootTable", at = @At("HEAD"))
    private void removeEnchantmentsFromDrops(CallbackInfo ci) {
        Mob mob = (Mob) (Object) this;

        // Remove enchantments from equipment
        for (ItemStack stack : mob.getHandSlots()) {
            EnchantmentHelper.setEnchantments(null, stack);
        }
        for (ItemStack stack : mob.getArmorSlots()) {
            EnchantmentHelper.setEnchantments(null, stack);
        }
    }

    @Inject(method = "enchantSpawnedArmor", at = @At("HEAD"), cancellable = true)
    private void removeEnchantArmorOnSpawn(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "enchantSpawnedWeapon", at = @At("HEAD"), cancellable = true)
    private void removeEnchantWeaponSpawn(CallbackInfo ci) {
        ci.cancel();
    }
}


