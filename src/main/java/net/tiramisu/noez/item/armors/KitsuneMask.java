package net.tiramisu.noez.item.armors;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.attribute.NoezAttributes;
import net.tiramisu.noez.item.ArmorAttribute;
import net.tiramisu.noez.sound.NoezSounds;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class KitsuneMask extends ArmorItem implements ArmorAttribute {
    private static final UUID KITSUNE_BONUS_REGEN = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final float HEAL_REGEN_BONUS = 5;
    private static final int DURATION = 120;

    public KitsuneMask(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("noez.kitsune_mask.tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.kitsune_mask.tooltip2"));
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof KitsuneMask && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD))) {
            EquipmentSlot slot = this.getType().getSlot();
            if (player.getItemBySlot(slot) == stack) {
                applyBonus(player, slot);
            } else {
                removeBonus(player, slot);
            }
            if (player.getHealth() < player.getMaxHealth() * 0.5 && player.getCooldowns().isOnCooldown(this)) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(), NoezSounds.KITSUNE_PROC.get(), SoundSource.PLAYERS, 1, 1);
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 8 * 20, 2));
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 8 * 20, 2));
                player.getCooldowns().addCooldown(this, DURATION * 20);
            }
        }
    }

    private void applyBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;
        AttributeModifier modifier;
        if (Objects.requireNonNull(slot) == EquipmentSlot.HEAD) {
            attributeInstance = player.getAttribute(NoezAttributes.HEALTH_REGENERATION.get());
            modifier = new AttributeModifier(KITSUNE_BONUS_REGEN, "Kitsune mask bonus", HEAL_REGEN_BONUS, AttributeModifier.Operation.ADDITION);
            applyModifier(attributeInstance, modifier);
        }
    }

    private void removeBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;
        if (Objects.requireNonNull(slot) == EquipmentSlot.HEAD) {
            attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
            removeModifier(attributeInstance, KITSUNE_BONUS_REGEN);
        }
    }

    private void applyModifier(AttributeInstance attributeInstance, AttributeModifier modifier) {
        if (attributeInstance != null && !attributeInstance.hasModifier(modifier)) {
            attributeInstance.addTransientModifier(modifier);
        }
    }

    private void removeModifier(AttributeInstance attributeInstance, UUID uuid) {
        if (attributeInstance != null && attributeInstance.getModifier(uuid) != null) {
            attributeInstance.removeModifier(uuid);
        }
    }

    public static boolean isBroken(ItemStack itemStack) {
        return itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1;
    }


    public String getTooltipID() {
        return "noez.kitsune_mask.tooltip";
    }
    public void setTooltipID(String tooltipID) {

    }
    public String helmetTooltip() {
        return "noez.heal_regen_bonus";
    }

    public String chesplateTooltip() {
        return "";
    }

    public String leggingsTooltip() {
        return "";
    }

    public String bootsTooltip() {
        return "";
    }

    public float helmetValue() {
        return HEAL_REGEN_BONUS;
    }
    public float chesplateValue() {
        return 0f;
    }

    public float leggingsValue() {
        return 0f;
    }
    public float bootsValue() {
        return 0f;
    }
}
