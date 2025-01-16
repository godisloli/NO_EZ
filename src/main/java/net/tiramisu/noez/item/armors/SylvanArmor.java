package net.tiramisu.noez.item.armors;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.tiramisu.noez.attribute.NoezAttributes;
import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.item.ArmorAttribute;
import net.tiramisu.noez.particles.NoezParticles;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class SylvanArmor extends ArmorItem implements ArmorAttribute {
    private String toolTipId = "none";
    private static final int HELMET_VALUE = 1;
    private static final int CHESTPLATE_VALUE = 6;
    private static final int LEGGINGS_VALUE = 4;
    private static final float BOOTS_VALUE = 1;
    private static float MAX_HEALTH_CAP = 40;
    private static final UUID HELMET_BONUS = UUID.fromString("12111161-1111-1111-1111-111111111111");
    private static final UUID CHESTPLATE_BONUS = UUID.fromString("23722222-2222-2222-2222-222222222222");
    private static final UUID LEGGINGS_BONUS = UUID.fromString("34335333-3333-3333-3333-333333333333");
    private static final UUID BOOTS_BONUS = UUID.fromString("44445442-4444-4444-4444-444444444444");
    private static final int COOLDOWN = 10 * 20;
    private static final int RADIUS = 4;
    private static final int DAMAGE = 3;

    public SylvanArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("noez.sylvan_" + toolTipId + ".tooltip1", MAX_HEALTH_CAP));
        pTooltipComponents.add(Component.translatable("noez.sylvan_" + toolTipId + ".tooltip2"));
        pTooltipComponents.add(Component.translatable("noez.sylvan_cooldown.tooltip", COOLDOWN / 20));
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (hasFullSylvanArmorSet(player)) {
            setTooltipID("full");
            fullSetBonus(player);
            halfSetBonus(player);
        }

        if (hasHalfSylvanArmorSet(player)) {
            setTooltipID("half");
            halfSetBonus(player);
        }

        if (!hasHalfSylvanArmorSet(player) && !hasFullSylvanArmorSet(player)) {
            setTooltipID("none");
        }

        if (!level.isClientSide) {
            EquipmentSlot slot = this.getType().getSlot();
            if (player.getItemBySlot(slot) == stack) {
                applyBonus(player, slot);
            } else {
                removeBonus(player, slot);
            }
        }
    }

    private void fullSetBonus(Player player) {
        if (player.level() instanceof ServerLevel serverLevel) {
            if (!player.getCooldowns().isOnCooldown(this)) {
                AABB area = new AABB(player.blockPosition()).inflate(RADIUS);

                List<LivingEntity> entities = serverLevel.getEntitiesOfClass(LivingEntity.class, area, entity -> entity != player &&
                        entity.getType().getCategory() == MobCategory.MONSTER);

                for (LivingEntity entity : entities) {
                    entity.hurt(player.damageSources().magic(), DAMAGE);

                    entity.addEffect(new MobEffectInstance(NoezEffects.ROOT.get(), 40, 2));
                }

                serverLevel.sendParticles(NoezParticles.SYLVAN_ROOT.get(), player.getX(), player.getY() + 0.1, player.getZ(), 1, 0, 0, 0, 0);
                serverLevel.playSound(null, player.blockPosition(), net.minecraft.sounds.SoundEvents.EVOKER_CAST_SPELL, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
                player.getCooldowns().addCooldown(this, COOLDOWN);
            }
        }
    }

    private void halfSetBonus(Player player) {
        if (player.getMaxHealth() >= MAX_HEALTH_CAP && player.level().isDay())
            heal(player);
    }

    private void heal(LivingEntity livingEntity) {
        livingEntity.heal(0.003125f + 0.000625f * (livingEntity.getMaxHealth() - livingEntity.getHealth()));
    }

    private static boolean hasFullSylvanArmorSet(Player player) {
        boolean hasHelmet = player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof SylvanArmor && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD));
        boolean hasChestplate = player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof SylvanArmor && !isBroken(player.getItemBySlot(EquipmentSlot.CHEST));
        boolean hasLeggings = player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof SylvanArmor && !isBroken(player.getItemBySlot(EquipmentSlot.LEGS));
        boolean hasBoots = player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof SylvanArmor && !isBroken(player.getItemBySlot(EquipmentSlot.FEET));

        return hasHelmet && hasChestplate && hasLeggings && hasBoots;
    }

    private static boolean hasHalfSylvanArmorSet(Player player) {
        int count = 0;
        count += player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof SylvanArmor && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof SylvanArmor && !isBroken(player.getItemBySlot(EquipmentSlot.CHEST)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof SylvanArmor && !isBroken(player.getItemBySlot(EquipmentSlot.LEGS)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof SylvanArmor && !isBroken(player.getItemBySlot(EquipmentSlot.FEET)) ? 1 : 0;

        return count >= 2 && count < 4;
    }

    private void applyBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;
        AttributeModifier modifier;

        switch (slot) {
            case HEAD:
                attributeInstance = player.getAttribute(NoezAttributes.HEALTH_REGENERATION.get());
                modifier = new AttributeModifier(HELMET_BONUS, "Sylvan helmet bonus", HELMET_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case CHEST:
                attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
                modifier = new AttributeModifier(CHESTPLATE_BONUS, "Sylvan chestplate bonus", CHESTPLATE_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case LEGS:
                attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
                modifier = new AttributeModifier(LEGGINGS_BONUS, "Sylvan leggings bonus", LEGGINGS_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case FEET:
                attributeInstance = player.getAttribute(NoezAttributes.HEALTH_REGENERATION.get());
                modifier = new AttributeModifier(BOOTS_BONUS, "Sylvan boots bonus", BOOTS_VALUE, AttributeModifier.Operation.ADDITION);
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
                attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
                removeModifier(attributeInstance, CHESTPLATE_BONUS);
                break;
            case LEGS:
                attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
                removeModifier(attributeInstance, LEGGINGS_BONUS);
                break;
            case FEET:
                attributeInstance = player.getAttribute(NoezAttributes.HEALTH_REGENERATION.get());
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
        return "noez.sylvan_" + toolTipId;
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
        return "noez.health_bonus";
    }

    @Override
    public String leggingsTooltip() {
        return "noez.health_bonus";
    }

    @Override
    public String bootsTooltip() {
        return "noez.heal_regen_bonus";
    }

    @Override
    public float helmetValue() {
        return HELMET_VALUE;
    }

    @Override
    public float chesplateValue() {
        return CHESTPLATE_VALUE;
    }

    @Override
    public float leggingsValue() {
        return LEGGINGS_VALUE;
    }

    @Override
    public float bootsValue() {
        return BOOTS_VALUE;
    }
}
