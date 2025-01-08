package net.tiramisu.noez.item.armors;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.attribute.NoezAttributes;
import net.tiramisu.noez.item.ArmorAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class EchoArmor extends ArmorItem implements ArmorAttribute {

    private String toolTipId = "none";
    private static final int RADIUS = 20;
    private static final int HELMET_VALUE = 2;
    private static final float CHESTPLATE_VALUE = 0.2f;
    private static final int LEGGINGS_VALUE = 4;
    private static final float BOOTS_VALUE = 0.04f;
    private static final UUID HELMET_BONUS = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID CHESTPLATE_BONUS = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID LEGGINGS_BONUS = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID BOOTS_BONUS = UUID.fromString("44444444-4444-4444-4444-444444444444");

    public EchoArmor(ArmorMaterial pMaterial, Type pType, Item.Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public String getTooltipID() {
        return "noez.echo_" + toolTipId;
    }

    @Override
    public void setTooltipID(String toolTipId) {
        this.toolTipId = toolTipId;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("noez.echo_" + toolTipId + ".tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.echo_" + toolTipId + ".tooltip2"));

    }

    public float helmetValue() {
        return HELMET_VALUE;
    }

    public float chesplateValue() {
        return CHESTPLATE_VALUE * 10;
    }

    public float leggingsValue(){
        return LEGGINGS_VALUE;
    }

    public float bootsValue() {
        return BOOTS_VALUE * 100;
    }

    private static boolean hasFullEchoArmorSet(Player player) {
        boolean hasHelmet = player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof EchoArmor && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD));
        boolean hasChestplate = player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof EchoArmor && !isBroken(player.getItemBySlot(EquipmentSlot.CHEST));
        boolean hasLeggings = player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof EchoArmor && !isBroken(player.getItemBySlot(EquipmentSlot.LEGS));
        boolean hasBoots = player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof EchoArmor && !isBroken(player.getItemBySlot(EquipmentSlot.FEET));

        return hasHelmet && hasChestplate && hasLeggings && hasBoots;
    }

    private static boolean hasHalfEchoArmorSet(Player player) {
        int count = 0;
        count += player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof EchoArmor && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof EchoArmor && !isBroken(player.getItemBySlot(EquipmentSlot.CHEST)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof EchoArmor && !isBroken(player.getItemBySlot(EquipmentSlot.LEGS)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof EchoArmor && !isBroken(player.getItemBySlot(EquipmentSlot.FEET)) ? 1 : 0;

        return count >= 2 && count < 4;
    }

    public static boolean isBroken(ItemStack itemStack) {
        return itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        return super.use(pLevel, pPlayer, pHand);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (hasFullEchoArmorSet(player)) {
            applyGlowingEffectInRadius(player, level);
            harden(player, level);
            setTooltipID("full");
        }
        if (hasHalfEchoArmorSet(player)) {
            applyGlowingEffectInRadius(player, level);
            setTooltipID("half");
        }
        if (!hasHalfEchoArmorSet(player) && !hasFullEchoArmorSet(player))
            setTooltipID("none");

        if (!level.isClientSide) {
            EquipmentSlot slot = this.getType().getSlot();
            if (player.getItemBySlot(slot) == stack) {
                applyBonus(player, slot);
            } else {
                removeBonus(player, slot);
            }
        }
    }

    private void applyGlowingEffectInRadius(Player player, Level level) {
        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate((float) EchoArmor.RADIUS), entity -> entity != player);
        for (LivingEntity entity : nearbyEntities) {
            if (!entity.hasEffect(MobEffects.GLOWING)) {
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0, false, false));
                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SCULK_CLICKING, SoundSource.NEUTRAL, 2.5f, 1f);
            }
        }
    }

    private void harden(Player player, Level level){
        if (!player.getCooldowns().isOnCooldown(this)) {
            if (level instanceof ServerLevel serverLevel) {
                double angle = Math.random() * 2 * Math.PI;
                double height = (Math.random() - 0.5) * 2;
                double distance = Math.sqrt( height * height);
                double offsetX = distance * Math.cos(angle);
                double offsetZ = distance * Math.sin(angle);
                double offsetY = height + 0.5;
                for (int i = 0; i < 8; i++)
                    serverLevel.sendParticles(
                        ParticleTypes.SCULK_SOUL,
                        player.getX() + offsetX,
                        player.getY() + offsetY,
                        player.getZ() + offsetZ,
                        1,
                        0,0,0,
                        0.1
                );
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WARDEN_DEATH, SoundSource.PLAYERS, 0.25f, 1f);
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 30 * 20, 1));
            player.getCooldowns().addCooldown(this, 30 * 20);
        }
    }

    private void applyBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;
        AttributeModifier modifier;

        switch (slot) {
            case HEAD:
                attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
                modifier = new AttributeModifier(HELMET_BONUS, "Echo helmet bonus", HELMET_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case CHEST:
                attributeInstance = player.getAttribute(NoezAttributes.PROJECTILE_REDUCTION.get());
                modifier = new AttributeModifier(CHESTPLATE_BONUS, "Echo chestplate bonus", CHESTPLATE_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case LEGS:
                attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
                modifier = new AttributeModifier(LEGGINGS_BONUS, "Echo leggings bonus", LEGGINGS_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case FEET:
                attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                modifier = new AttributeModifier(BOOTS_BONUS, "Echo boots bonus", BOOTS_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
        }
    }

    private void removeBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;

        switch (slot) {
            case HEAD:
                attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
                removeModifier(attributeInstance, HELMET_BONUS);
                break;
            case CHEST:
                attributeInstance = player.getAttribute(NoezAttributes.PROJECTILE_REDUCTION.get());
                removeModifier(attributeInstance, CHESTPLATE_BONUS);
                break;
            case LEGS:
                attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
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

    public String helmetTooltip() {
        return "noez.health_bonus";
    }

    public String chesplateTooltip() {
        return "noez.projectile_reduction_bonus";
    }

    public String leggingsTooltip() {
        return "noez.health_bonus";
    }

    public String bootsTooltip() {
        return "noez.movement_speed_bonus";
    }
}
