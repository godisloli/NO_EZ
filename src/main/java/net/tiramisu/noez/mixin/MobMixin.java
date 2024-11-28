package net.tiramisu.noez.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.effect.NoezEffects;
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
    private void onTick(CallbackInfo info) {
        // Cast this mixin's instance to Mob
        Mob mob = (Mob) (Object) this;

        // Skip if the mob is targeting something already
        if (mob.getTarget() != null)
            return;

        if (mob instanceof Warden)
            return;

        // Get the world instance
        Level level = mob.level();

        // Check for nearby players
        for (Player player : level.getEntitiesOfClass(Player.class, mob.getBoundingBox().inflate(MAX_RANGE))) {
            // Skip dead players or spectators
            if (player.isSpectator() || player.isDeadOrDying()) {
                continue;
            }

            double distance = mob.distanceTo(player);
            boolean isSneaking = player.isCrouching();

            if (!isSneaking) {
                // Player is not sneaking - always turn
                turnMobToPlayer(mob, player);
                return;
            } else {
                // Player is sneaking - chance to turn
                double chance = (distance <= CLOSE_RANGE) ? 0.02 : 0.005; // 40% for close range, 10% for max range
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
}
