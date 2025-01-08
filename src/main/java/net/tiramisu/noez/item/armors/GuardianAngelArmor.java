package net.tiramisu.noez.item.armors;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.attribute.NoezAttributes;
import net.tiramisu.noez.item.ArmorAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class GuardianAngelArmor extends ArmorItem implements ArmorAttribute {
    private String toolTipId = "none";
    private static final int HELMET_VALUE = 1;
    private static final int CHESTPLATE_VALUE = 20;
    private static final int LEGGINGS_VALUE = 2;
    private static final float BOOTS_VALUE = 20f;
    private static final UUID HELMET_BONUS = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID CHESTPLATE_BONUS = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID LEGGINGS_BONUS = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID BOOTS_BONUS = UUID.fromString("44444444-4444-4444-4444-444444444444");
    private static final int COOLDOWN = 900 * 20;

    public GuardianAngelArmor(ArmorMaterial pMaterial, Type pType, Item.Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void setTooltipID(String toolTipId) {
        this.toolTipId = toolTipId;
    }

    @Override
    public String getTooltipID() {
        return "noez.guardian_angel_" + toolTipId;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("noez.guardian_angel_" + toolTipId + ".tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.guardian_angel_" + toolTipId + ".tooltip2"));
        pTooltipComponents.add(Component.translatable("noez.guardian_angel_cooldown.tooltip", COOLDOWN / 1200));

    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (hasFullGuardianAngelArmorSet(player)) {
            setTooltipID("full");
        }
        if (hasHalfGuardianAngelArmorSet(player)) {
            setTooltipID("half");

        }
        if (!hasHalfGuardianAngelArmorSet(player) && !hasFullGuardianAngelArmorSet(player))
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

    @SubscribeEvent
    public static void guardianProtection(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer && hasFullGuardianAngelArmorSet(serverPlayer)) {
            if (!isAnyPieceOnCooldown(serverPlayer)) {
                event.setCanceled(true);
                activateTotemEffect(serverPlayer);
                applyCooldownToAllArmorPieces(serverPlayer);
            }
        }
    }

    private static boolean isAnyPieceOnCooldown(ServerPlayer serverPlayer) {
        ItemStack helmet = serverPlayer.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = serverPlayer.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = serverPlayer.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = serverPlayer.getItemBySlot(EquipmentSlot.FEET);

        return isItemOnCooldown(serverPlayer, helmet) || isItemOnCooldown(serverPlayer, chestplate) ||
                isItemOnCooldown(serverPlayer, leggings) || isItemOnCooldown(serverPlayer, boots);
    }

    private static boolean isItemOnCooldown(ServerPlayer serverPlayer, ItemStack itemStack) {
        return itemStack.getItem() instanceof GuardianAngelArmor && serverPlayer.getCooldowns().isOnCooldown(itemStack.getItem());
    }

    private static void applyCooldownToAllArmorPieces(ServerPlayer serverPlayer) {
        ItemStack helmet = serverPlayer.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = serverPlayer.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = serverPlayer.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = serverPlayer.getItemBySlot(EquipmentSlot.FEET);

        if (helmet.getItem() instanceof GuardianAngelArmor) {
            serverPlayer.getCooldowns().addCooldown(helmet.getItem(), COOLDOWN);
        }
        if (chestplate.getItem() instanceof GuardianAngelArmor) {
            serverPlayer.getCooldowns().addCooldown(chestplate.getItem(), COOLDOWN);
        }
        if (leggings.getItem() instanceof GuardianAngelArmor) {
            serverPlayer.getCooldowns().addCooldown(leggings.getItem(), COOLDOWN);
        }
        if (boots.getItem() instanceof GuardianAngelArmor) {
            serverPlayer.getCooldowns().addCooldown(boots.getItem(), COOLDOWN);
        }
    }

    private static void activateTotemEffect(Player player) {
        if (player.level() instanceof ServerLevel serverLevel) {
            player.setHealth(1.0F);

            player.removeAllEffects();

            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));

            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

            serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                    player.getX(), player.getY() + 1.0D, player.getZ(),
                    30, 0.5D, 0.5D, 0.5D, 0.0D);
        }
    }

    private static boolean hasFullGuardianAngelArmorSet(Player player) {
        boolean hasHelmet = player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof GuardianAngelArmor && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD));
        boolean hasChestplate = player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof GuardianAngelArmor && !isBroken(player.getItemBySlot(EquipmentSlot.CHEST));
        boolean hasLeggings = player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof GuardianAngelArmor && !isBroken(player.getItemBySlot(EquipmentSlot.LEGS));
        boolean hasBoots = player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof GuardianAngelArmor && !isBroken(player.getItemBySlot(EquipmentSlot.FEET));

        return hasHelmet && hasChestplate && hasLeggings && hasBoots;
    }

    private static boolean hasHalfGuardianAngelArmorSet(Player player) {
        int count = 0;
        count += player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof GuardianAngelArmor && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof GuardianAngelArmor && !isBroken(player.getItemBySlot(EquipmentSlot.CHEST)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof GuardianAngelArmor && !isBroken(player.getItemBySlot(EquipmentSlot.LEGS)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof GuardianAngelArmor && !isBroken(player.getItemBySlot(EquipmentSlot.FEET)) ? 1 : 0;

        return count >= 2 && count < 4;
    }

    private void applyBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;
        AttributeModifier modifier;

        switch (slot) {
            case HEAD:
                attributeInstance = player.getAttribute(Attributes.ATTACK_DAMAGE);
                modifier = new AttributeModifier(HELMET_BONUS, "Guardian Angel helmet bonus", HELMET_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case CHEST:
                attributeInstance = player.getAttribute(NoezAttributes.MAGIC_REDUCTION.get());
                modifier = new AttributeModifier(CHESTPLATE_BONUS, "Guardian Angel chestplate bonus", CHESTPLATE_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case LEGS:
                attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
                modifier = new AttributeModifier(LEGGINGS_BONUS, "Guardian Angel leggings bonus", LEGGINGS_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case FEET:
                attributeInstance = player.getAttribute(NoezAttributes.CRIT_CHANCE.get());
                modifier = new AttributeModifier(BOOTS_BONUS, "Guardian Angel boots bonus", BOOTS_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
        }
    }

    private void removeBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;

        switch (slot) {
            case HEAD:
                attributeInstance = player.getAttribute(Attributes.ATTACK_DAMAGE);
                removeModifier(attributeInstance, HELMET_BONUS);
                break;
            case CHEST:
                attributeInstance = player.getAttribute(NoezAttributes.MAGIC_REDUCTION.get());
                removeModifier(attributeInstance, CHESTPLATE_BONUS);
                break;
            case LEGS:
                attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
                removeModifier(attributeInstance, LEGGINGS_BONUS);
                break;
            case FEET:
                attributeInstance = player.getAttribute(NoezAttributes.CRIT_CHANCE.get());
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

    public float helmetValue() {
        return HELMET_VALUE;
    }

    public float chesplateValue() {
        return CHESTPLATE_VALUE;
    }

    public float leggingsValue(){
        return LEGGINGS_VALUE;
    }

    public float bootsValue() {
        return BOOTS_VALUE;
    }

    public static boolean isBroken(ItemStack itemStack) {
        return itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1;
    }

    public String helmetTooltip() {
        return "noez.damage_bonus";
    }

    public String chesplateTooltip() {
        return "noez.magic_resistant_bonus";
    }

    public String leggingsTooltip() {
        return "noez.health_bonus";
    }

    public String bootsTooltip() {
        return "noez.crit_bonus";
    }
}
