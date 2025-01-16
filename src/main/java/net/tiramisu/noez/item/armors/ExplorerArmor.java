package net.tiramisu.noez.item.armors;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.attribute.NoezAttributes;
import net.tiramisu.noez.item.ArmorAttribute;

import java.util.UUID;

public class ExplorerArmor extends ArmorItem implements ArmorAttribute {
    private String toolTipId = "none";
    private static final float HELMET_VALUE = 1;
    private static final float CHESTPLATE_VALUE = 0.2f;
    private static final float LEGGINGS_VALUE = 0.04f;
    private static final float BOOTS_VALUE = 0.02f;
    private static final UUID HELMET_BONUS = UUID.fromString("12111311-1111-1111-1111-111111111211");
    private static final UUID CHESTPLATE_BONUS = UUID.fromString("23522222-2222-2222-2222-226222222222");
    private static final UUID LEGGINGS_BONUS = UUID.fromString("34332333-3333-3333-3333-333333333633");
    private static final UUID BOOTS_BONUS = UUID.fromString("44445414-4444-4444-4444-444444446444");
    
    public ExplorerArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (!level.isClientSide) {
            EquipmentSlot slot = this.getType().getSlot();
            if (player.getItemBySlot(slot) == stack) {
                applyBonus(player, slot);
            } else {
                removeBonus(player, slot);
            }
        }
    }

    private void applyBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;
        AttributeModifier modifier;

        switch (slot) {
            case HEAD:
                attributeInstance = player.getAttribute(NoezAttributes.HEALTH_REGENERATION.get());
                modifier = new AttributeModifier(HELMET_BONUS, "Explorer helmet bonus", HELMET_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case CHEST:
                attributeInstance = player.getAttribute(Attributes.ATTACK_SPEED);
                modifier = new AttributeModifier(CHESTPLATE_BONUS, "Explorer chestplate bonus", CHESTPLATE_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case LEGS:
                attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                modifier = new AttributeModifier(LEGGINGS_BONUS, "Explorer leggings bonus", LEGGINGS_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case FEET:
                attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                modifier = new AttributeModifier(BOOTS_BONUS, "Explorer boots bonus", BOOTS_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
        }
    }

    private void removeBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;

        switch (slot) {
            case HEAD:
                attributeInstance = player.getAttribute(NoezAttributes.HEALTH_REGENERATION.get());
                removeModifier(attributeInstance, HELMET_BONUS);
                break;
            case CHEST:
                attributeInstance = player.getAttribute(Attributes.ATTACK_SPEED);
                removeModifier(attributeInstance, CHESTPLATE_BONUS);
                break;
            case LEGS:
                attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                removeModifier(attributeInstance, LEGGINGS_BONUS);
                break;
            case FEET:
                attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                removeModifier(attributeInstance, BOOTS_BONUS);
                break;
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

    @Override
    public String getTooltipID() {
        return "noez.explorer_" + toolTipId;
    }

    @Override
    public void setTooltipID(String tooltipID) {
        this.toolTipId = tooltipID;
    }

    @Override
    public String helmetTooltip() {
        return "noez.heal_regen_bonus";
    }

    @Override
    public String chesplateTooltip() {
        return "noez.attack_speed_bonus";
    }

    @Override
    public String leggingsTooltip() {
        return "noez.movement_speed_bonus";
    }

    @Override
    public String bootsTooltip() {
        return "noez.movement_speed_bonus";
    }

    @Override
    public float helmetValue() {
        return HELMET_VALUE;
    }

    @Override
    public float chesplateValue() {
        return CHESTPLATE_VALUE * 100;
    }

    @Override
    public float leggingsValue() {
        return LEGGINGS_VALUE * 100;
    }

    @Override
    public float bootsValue() {
        return BOOTS_VALUE * 100;
    }
}
