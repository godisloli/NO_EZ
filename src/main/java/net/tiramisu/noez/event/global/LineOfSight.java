package net.tiramisu.noez.event.global;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.util.NoezTags;

@Mod.EventBusSubscriber()
public class LineOfSight {

    public static boolean isLookingAtYou(LivingEntity entity, Entity target) {

        if (entity.getType().is(NoezTags.Mobs.NO_LINE_OF_SIGHT))
            return true;

        float nearDistance = 5.0F;
        double farDistance;
        try {
            farDistance = entity.getAttribute(Attributes.FOLLOW_RANGE).getValue();
        } catch (Exception e){
            farDistance = 16f;
        }

        float largeAngle = 83.0F;
        float smallAngle = 30.0F;
        float mobViewThreshold = 10.0F;

        if (entity.hasEffect(MobEffects.INVISIBILITY))
            return false;

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
        return proximity > mobViewThreshold;
    }

    public static boolean canSeeThroughBlocks(LivingEntity entity, Entity target) {
        Level level = entity.level();
        Vec3 entityPosition = entity.position();
        Vec3 targetPosition = target.position();
        ClipContext context = new ClipContext(entityPosition, targetPosition, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity);
        BlockHitResult result = level.clip(context);

        if (result.getType() != BlockHitResult.Type.MISS) {
            BlockState blockState = level.getBlockState(result.getBlockPos());
            return !blockState.isSolid();
        }
        return true;
    }
}
