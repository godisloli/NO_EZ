package net.tiramisu.noez.event;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class LineOfSight {

    public static boolean isLookingAtYou(LivingEntity entity, Entity target) {
        float nearDistance = LineOfSightConfig.mobDistanceThreshold;
        double farDistance = entity.getAttribute(Attributes.FOLLOW_RANGE).getValue();
        float largeAngle = 83.0F; // Outer boundary of vision cone
        float smallAngle = 30.0F; // Inner boundary of vision cone

        // Get direction vectors
        Vec3 entityLookDirection = entity.getViewVector(1.0F).normalize(); // Where the entity is looking
        Vec3 directionToTarget = new Vec3(
                target.getX() - entity.getX(),
                target.getY() - entity.getY(),
                target.getZ() - entity.getZ()
        ).normalize(); // Direction from entity to target

        // Calculate angle difference (dot product)
        double alignment = entityLookDirection.dot(directionToTarget);
        double angleDifference = alignment * largeAngle;

        // If the target is outside the field of view, return false
        if (angleDifference > largeAngle) {
            return false;
        }

        // Calculate dynamic distance threshold based on the angle
        double distanceThreshold;
        if (angleDifference < smallAngle) {
            distanceThreshold = farDistance;
        } else {
            distanceThreshold = nearDistance + (farDistance - nearDistance) * (largeAngle - angleDifference) / (largeAngle - smallAngle);
        }

        // Calculate proximity (distance from entity to target)
        double proximity = entityLookDirection.distanceTo(target.position()) / distanceThreshold;

        // Check if the target is within the view threshold
        double sensitivity = LineOfSightConfig.mobViewThreshold;
        return proximity > sensitivity;
    }

    public class LineOfSightConfig {
        public static float mobViewThreshold = 20.0F;
        public static float mobDistanceThreshold = 10.0F;

        public LineOfSightConfig() {
        }
    }
}
