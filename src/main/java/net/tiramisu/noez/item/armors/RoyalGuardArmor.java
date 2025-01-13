package net.tiramisu.noez.item.armors;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.attribute.NoezAttributes;
import net.tiramisu.noez.item.ArmorAttribute;
import net.tiramisu.noez.particles.NoezParticles;
import net.tiramisu.noez.sound.NoezSounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class RoyalGuardArmor extends ArmorItem implements ArmorAttribute {
    private String toolTipId = "none";
    private static final int HELMET_VALUE = 2;
    private static final int CHESTPLATE_VALUE = 4;
    private static final float LEGGINGS_VALUE = 20;
    private static final float BOOTS_VALUE = 15;
    private static final UUID HELMET_BONUS = UUID.fromString("12112111-1111-1111-1111-111111111111");
    private static final UUID CHESTPLATE_BONUS = UUID.fromString("23222322-2222-2222-2222-222222222222");
    private static final UUID LEGGINGS_BONUS = UUID.fromString("34333433-3333-3333-3333-333333333333");
    private static final UUID BOOTS_BONUS = UUID.fromString("45445444-4444-4444-4444-444444444444");
    private static final int COOLDOWN = 120 * 20;
    private float cumulativeDamage = 0;

    public RoyalGuardArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("noez.royal_guard_" + toolTipId + ".tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.royal_guard_" + toolTipId + ".tooltip2"));
        pTooltipComponents.add(Component.translatable("noez.royal_guard_cooldown.tooltip", COOLDOWN / 20));
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (hasFullRoyalGuardArmorSet(player)) {
            setTooltipID("full");
            fullSetBonus(player);
        }

        if (hasHalfRoyalGuardArmorSet(player)) {
            setTooltipID("half");
        }

        if (!hasHalfRoyalGuardArmorSet(player) && !hasFullRoyalGuardArmorSet(player)) {
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

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (hasHalfRoyalGuardArmorSet(player) || hasFullRoyalGuardArmorSet(player)) {
                RoyalGuardArmor armor = (RoyalGuardArmor) player.getItemBySlot(EquipmentSlot.CHEST).getItem();
                armor.addCumulativeDamage(player, event.getAmount());
            }
        }
    }

    private void addCumulativeDamage(Player player, float damage) {
        cumulativeDamage += damage;
        System.out.println(cumulativeDamage);
        if (cumulativeDamage >= 30) {
            triggerAoEDamage(player);
            cumulativeDamage = 0;
        }
    }

    private void triggerAoEDamage(Player player) {
        Level level = player.level();
        float aoeDamage = cumulativeDamage / 6f;

        List<Entity> nearbyEntities = level.getEntities(player, player.getBoundingBox().inflate(6),
                entity -> entity instanceof LivingEntity && entity != player);

        if (!nearbyEntities.isEmpty() && level instanceof ServerLevel serverLevel) {
            for (Entity entity : nearbyEntities) {
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.hurt(player.damageSources().explosion(null), aoeDamage);
                    double dx = livingEntity.getX() - player.getX();
                    double dz = livingEntity.getZ() - player.getZ();
                    double distance = Math.sqrt(dx * dx + dz * dz);

                    if (distance > 0) {
                        double knockbackStrength = 1;
                        livingEntity.push(
                                dx / distance * knockbackStrength,
                                0.5,
                                dz / distance * knockbackStrength
                        );
                    }
                }
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), NoezSounds.ROYAL_GUARDIAN_EXPLODE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            serverLevel.sendParticles(NoezParticles.ROYAL_EXPLOSION.get(), player.getX(), player.getY() + 0.2, player.getZ(), 1, 0, 0, 0, 0);
        }
    }

    private void fullSetBonus(Player player) {
        if (player.getHealth() < player.getMaxHealth() * 0.3 && !player.getCooldowns().isOnCooldown(this)) {
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 15 * 20, 3));
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), NoezSounds.ROYAL_GUARDIAN_PROC.get(), SoundSource.PLAYERS, 1, 1);
            player.getCooldowns().addCooldown(this, COOLDOWN);
        }
    }

    private static boolean hasFullRoyalGuardArmorSet(Player player) {
        boolean hasHelmet = player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof RoyalGuardArmor && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD));
        boolean hasChestplate = player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof RoyalGuardArmor && !isBroken(player.getItemBySlot(EquipmentSlot.CHEST));
        boolean hasLeggings = player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof RoyalGuardArmor && !isBroken(player.getItemBySlot(EquipmentSlot.LEGS));
        boolean hasBoots = player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof RoyalGuardArmor && !isBroken(player.getItemBySlot(EquipmentSlot.FEET));

        return hasHelmet && hasChestplate && hasLeggings && hasBoots;
    }

    private static boolean hasHalfRoyalGuardArmorSet(Player player) {
        int count = 0;
        count += player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof RoyalGuardArmor && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof RoyalGuardArmor && !isBroken(player.getItemBySlot(EquipmentSlot.CHEST)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof RoyalGuardArmor && !isBroken(player.getItemBySlot(EquipmentSlot.LEGS)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof RoyalGuardArmor && !isBroken(player.getItemBySlot(EquipmentSlot.FEET)) ? 1 : 0;

        return count >= 2 && count < 4;
    }

    private void applyBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;
        AttributeModifier modifier;

        switch (slot) {
            case HEAD:
                attributeInstance = player.getAttribute(NoezAttributes.HEALTH_REGENERATION.get());
                modifier = new AttributeModifier(HELMET_BONUS, "Royal Guard helmet bonus", HELMET_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case CHEST:
                attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
                modifier = new AttributeModifier(CHESTPLATE_BONUS, "Royal Guard chestplate bonus", CHESTPLATE_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case LEGS:
                attributeInstance = player.getAttribute(NoezAttributes.PROJECTILE_REDUCTION.get());
                modifier = new AttributeModifier(LEGGINGS_BONUS, "Royal Guard leggings bonus", LEGGINGS_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case FEET:
                attributeInstance = player.getAttribute(NoezAttributes.MAGIC_REDUCTION.get());
                modifier = new AttributeModifier(BOOTS_BONUS, "Royal Guard boots bonus", BOOTS_VALUE, AttributeModifier.Operation.ADDITION);
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
                attributeInstance = player.getAttribute(NoezAttributes.PROJECTILE_REDUCTION.get());
                removeModifier(attributeInstance, LEGGINGS_BONUS);
                break;
            case FEET:
                attributeInstance = player.getAttribute(NoezAttributes.MAGIC_REDUCTION.get());
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
        return "noez.royal_guard_" + toolTipId;
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
        return "noez.projectile_reduction_bonus";
    }

    @Override
    public String bootsTooltip() {
        return "noez.magic_resistant_bonus";
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
