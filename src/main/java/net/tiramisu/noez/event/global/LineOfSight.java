package net.tiramisu.noez.event.global;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class LineOfSight {

    public static boolean isLookingAtYou(LivingEntity entity, Entity target) {
        float nearDistance = 5.0F;
        double farDistance;
        try {
            farDistance = entity.getAttribute(Attributes.FOLLOW_RANGE).getValue();
        } catch (Exception e){
            farDistance = 10f;
        }
        float largeAngle = 83.0F; // Outer boundary of vision cone
        float smallAngle = 50.0F; // Inner boundary of vision cone
        float mobViewThreshold = 20.0F;

        if (target instanceof LivingEntity livingEntity){
            if (livingEntity.hasEffect(MobEffects.INVISIBILITY))
                return false;
        }

        Vec3 entityLookDirection = entity.getViewVector(1.0F).normalize();
        Vec3 directionToTarget = new Vec3(
                target.getX() - entity.getX(),
                target.getY() - entity.getY(),
                target.getZ() - entity.getZ()
        ).normalize();

        double alignment = entityLookDirection.dot(directionToTarget);
        double angleDifference = alignment * largeAngle;

        if (angleDifference > largeAngle) {
            return false;
        }

        double distanceThreshold;
        if (angleDifference < smallAngle) {
            distanceThreshold = farDistance;
        } else {
            distanceThreshold = nearDistance + (farDistance - nearDistance) * (largeAngle - angleDifference) / (largeAngle - smallAngle);
        }

        double proximity = entityLookDirection.distanceTo(target.position()) / distanceThreshold;

        double sensitivity = mobViewThreshold;
        return proximity > sensitivity;
    }
}
