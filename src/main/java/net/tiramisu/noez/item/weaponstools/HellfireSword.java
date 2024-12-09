package net.tiramisu.noez.item.weaponstools;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class HellfireSword extends SwordItem {

    private static final UUID DAMAGE_BOOST_UUID = UUID.fromString("e2b10e7f-7b55-4a76-b3d5-95e5d9f7ea27");
    private static final double DAMAGE_BOOST_PER_TARGET = 1.25; // bonus attack damage per unique target
    private static final double MAX_DAMAGE_BOOST = 5.0;        // maximum bonus attack damage
    private static final int BONUS_REMOVAL_DELAY = 100;        // ticks
    private final Set<UUID> affectedTargets = new HashSet<>();
    private long lastAttackTime = 0;

    public HellfireSword(Tier tier, int Damage, float AttackSpeed, Properties properties) {
        super(tier, Damage, AttackSpeed, properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("noez.hellfire_sword.tooltip1", DAMAGE_BOOST_PER_TARGET, MAX_DAMAGE_BOOST));
        pTooltipComponents.add(Component.translatable("noez.hellfire_sword.tooltip2", BONUS_REMOVAL_DELAY / 20));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!(pStack.getDamageValue() == pStack.getMaxDamage() - 1) && !pTarget.fireImmune()) {
            if (!pTarget.level().isClientSide()) {
                pAttacker.level().playSound(
                        null,
                        pTarget.getX(),pTarget.getY(),pTarget.getZ(),
                        SoundEvents.FIRECHARGE_USE,
                        SoundSource.PLAYERS,
                        1.0f,
                        1.0f
                );
                pTarget.setSecondsOnFire(4);
                applyDamageBoost(pAttacker, pTarget);
                lastAttackTime = pAttacker.level().getGameTime();
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    private void applyDamageBoost(LivingEntity pAttacker, LivingEntity pTarget) {
        UUID targetUUID = pTarget.getUUID();
        if (!affectedTargets.contains(targetUUID)) {
            affectedTargets.add(targetUUID);
            AttributeModifier existingModifier = pAttacker.getAttribute(Attributes.ATTACK_DAMAGE)
                    .getModifier(DAMAGE_BOOST_UUID);
            double currentBoost = existingModifier != null ? existingModifier.getAmount() : 0.0;
            if (currentBoost < MAX_DAMAGE_BOOST) {
                double newBoost = Math.min(currentBoost + DAMAGE_BOOST_PER_TARGET, MAX_DAMAGE_BOOST);
                if (existingModifier != null) {
                    pAttacker.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(DAMAGE_BOOST_UUID);
                }
                pAttacker.getAttribute(Attributes.ATTACK_DAMAGE)
                        .addPermanentModifier(new AttributeModifier(
                                DAMAGE_BOOST_UUID,
                                "Hellfire Sword Damage Boost",
                                newBoost,
                                AttributeModifier.Operation.ADDITION
                        ));
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (!pLevel.isClientSide() && pEntity instanceof LivingEntity livingEntity) {
            long currentTime = pLevel.getGameTime();
            boolean isHoldingSword = livingEntity.getMainHandItem().getItem() == this;

            if (isHoldingSword) {
                if (currentTime - lastAttackTime > BONUS_REMOVAL_DELAY) {
                    removeDamageBoost(livingEntity);
                    affectedTargets.clear();
                }
            } else {
                removeDamageBoost(livingEntity);
                affectedTargets.clear();
            }
        }
    }

    private void removeDamageBoost(LivingEntity pAttacker) {
        AttributeModifier existingModifier = pAttacker.getAttribute(Attributes.ATTACK_DAMAGE)
                .getModifier(DAMAGE_BOOST_UUID);
        if (existingModifier != null) {
            pAttacker.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(DAMAGE_BOOST_UUID);
        }
    }
}
