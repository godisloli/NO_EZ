package net.tiramisu.noez.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.tiramisu.noez.attribute.NoezAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class VillagerMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addAvoidPlayerGoal(CallbackInfo ci) {
        Villager villager = (Villager) (Object) this;

        villager.goalSelector.addGoal(3, new AvoidEntityGoal<>(villager, Player.class,
                8.0F,
                0.5D,
                0.75D,
                this::shouldVillagerRunAway));
    }

    private boolean shouldVillagerRunAway(LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            AttributeInstance reputationAttr = player.getAttribute(NoezAttributes.REPUTATION.get());
            return reputationAttr != null && reputationAttr.getValue() <= 200;
        }
        return false;
    }
}
