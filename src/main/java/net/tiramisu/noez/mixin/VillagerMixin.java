package net.tiramisu.noez.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.tiramisu.noez.attribute.NoezAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class VillagerMixin {
    @Inject(method = "registerBrainGoals", at = @At("TAIL"))
    private void addFearBehavior(CallbackInfo ci) {
        Villager villager = (Villager) (Object) this;
        if (villager.goalSelector != null) {
            villager.goalSelector.addGoal(0, new AvoidEntityGoal<>(
                    villager,
                    Player.class,
                    8.0F,
                    1.0D,
                    0.8D,
                    this::shouldVillagerAvoid
            ));
        }
    }

    private boolean shouldVillagerAvoid(LivingEntity entity) {
        if (entity instanceof Player player) {
            if (player.getAttributes().hasAttribute(NoezAttributes.REPUTATION.get())) {
                double reputation = player.getAttributeValue(NoezAttributes.REPUTATION.get());
                return reputation < -200;
            }
        }
        return false;
    }
}
