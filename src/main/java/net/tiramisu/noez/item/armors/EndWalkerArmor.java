package net.tiramisu.noez.item.armors;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.attribute.NoezAttributes;
import net.tiramisu.noez.item.ArmorAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class EndWalkerArmor extends ArmorItem implements ArmorAttribute {
    private String toolTipId = "none";
    private static final float HELMET_VALUE = 0.25f;
    private static final int CHESTPLATE_VALUE = 2;
    private static final float LEGGINGS_VALUE = 0.25f;
    private static final float BOOTS_VALUE = 0.15f;
    private static final UUID HELMET_BONUS = UUID.fromString("12111311-1111-1111-1111-111111141111");
    private static final UUID CHESTPLATE_BONUS = UUID.fromString("23225222-2222-2222-2222-222223222222");
    private static final UUID LEGGINGS_BONUS = UUID.fromString("34333373-3333-3333-3333-333333383333");
    private static final UUID BOOTS_BONUS = UUID.fromString("42445444-4444-4444-4444-444444454444");
    private static final UUID HALF_SET_BONUS_1 = UUID.fromString("13112413-7222-3233-4434-555556555555");
    private static final UUID HALF_SET_BONUS_2 = UUID.fromString("13142413-7223-3231-4434-555556555555");


    public EndWalkerArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void setTooltipID(String toolTipId) {
        this.toolTipId = toolTipId;
    }

    @Override
    public String getTooltipID() {
        return "noez.end_walker_" + toolTipId;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("noez.end_walker_" + toolTipId + ".tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.end_walker_" + toolTipId + ".tooltip2"));
    }
    
    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (hasFullEndWalkerArmorSet(player)) {
            setTooltipID("full");
            halfSetBonus(player);
        }

        if (hasHalfEndWalkerArmorSet(player)) {
            setTooltipID("half");
            halfSetBonus(player);
        }

        if (!hasHalfEndWalkerArmorSet(player) && !hasFullEndWalkerArmorSet(player)) {
            setTooltipID("none");
            removeModifier(player.getAttribute(NoezAttributes.CRIT_DAMAGE.get()), HALF_SET_BONUS_1);
            removeModifier(player.getAttribute(NoezAttributes.CRIT_CHANCE.get()), HALF_SET_BONUS_2);
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
    public static void fullSetActive(ProjectileImpactEvent event) {
        if (event.getEntity() instanceof ThrownEnderpearl enderPearl) {
            if (enderPearl.getOwner() instanceof ServerPlayer serverPlayer && hasFullEndWalkerArmorSet(serverPlayer)) {
                MobEffectInstance currentEffect = serverPlayer.getEffect(MobEffects.DAMAGE_BOOST);
                if (currentEffect != null) {
                    int currentAmplifier = currentEffect.getAmplifier();
                    serverPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, Math.min(currentAmplifier + 1, 2)));
                } else {
                    serverPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0));
                }
            }
        }
    }

    private void halfSetBonus(Player player) {
        if (player.getHealth() < player.getMaxHealth() * 0.5) {
            AttributeInstance attributeInstance1;
            AttributeModifier modifier1;
            attributeInstance1 = player.getAttribute(NoezAttributes.CRIT_DAMAGE.get());
            modifier1 = new AttributeModifier(HALF_SET_BONUS_1, "End Walker half set bonus 1", 0.25, AttributeModifier.Operation.ADDITION);
            applyModifier(attributeInstance1, modifier1);

            AttributeInstance attributeInstance2;
            AttributeModifier modifier2;
            attributeInstance2 = player.getAttribute(NoezAttributes.CRIT_CHANCE.get());
            modifier2 = new AttributeModifier(HALF_SET_BONUS_2, "End Walker half set bonus 2", 0.25, AttributeModifier.Operation.ADDITION);
            applyModifier(attributeInstance2, modifier2);

            if (player.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.REVERSE_PORTAL,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        3,
                        0.5, 0.5, 0.5,
                        0.1
                );
            }
        }
    }

    private static boolean hasFullEndWalkerArmorSet(Player player) {
        boolean hasHelmet = player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof EndWalkerArmor && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD));
        boolean hasChestplate = player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof EndWalkerArmor && !isBroken(player.getItemBySlot(EquipmentSlot.CHEST));
        boolean hasLeggings = player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof EndWalkerArmor && !isBroken(player.getItemBySlot(EquipmentSlot.LEGS));
        boolean hasBoots = player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof EndWalkerArmor && !isBroken(player.getItemBySlot(EquipmentSlot.FEET));

        return hasHelmet && hasChestplate && hasLeggings && hasBoots;
    }

    private static boolean hasHalfEndWalkerArmorSet(Player player) {
        int count = 0;
        count += player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof EndWalkerArmor && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof EndWalkerArmor && !isBroken(player.getItemBySlot(EquipmentSlot.CHEST)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof EndWalkerArmor && !isBroken(player.getItemBySlot(EquipmentSlot.LEGS)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof EndWalkerArmor && !isBroken(player.getItemBySlot(EquipmentSlot.FEET)) ? 1 : 0;
        return count >= 2 && count < 4;
    }

    private void applyBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;
        AttributeModifier modifier;

        switch (slot) {
            case HEAD:
                attributeInstance = player.getAttribute(Attributes.ATTACK_SPEED);
                modifier = new AttributeModifier(HELMET_BONUS, "End Walker helmet bonus", HELMET_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case CHEST:
                attributeInstance = player.getAttribute(Attributes.ATTACK_DAMAGE);
                modifier = new AttributeModifier(CHESTPLATE_BONUS, "End Walker chestplate bonus", CHESTPLATE_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case LEGS:
                attributeInstance = player.getAttribute(NoezAttributes.CRIT_DAMAGE.get());
                modifier = new AttributeModifier(LEGGINGS_BONUS, "End Walker leggings bonus", LEGGINGS_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case FEET:
                attributeInstance = player.getAttribute(NoezAttributes.CRIT_CHANCE.get());
                modifier = new AttributeModifier(BOOTS_BONUS, "End Walker boots bonus", BOOTS_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
        }
    }

    private void removeBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;

        switch (slot) {
            case HEAD:
                attributeInstance = player.getAttribute(Attributes.ATTACK_SPEED);
                removeModifier(attributeInstance, HELMET_BONUS);
                break;
            case CHEST:
                attributeInstance = player.getAttribute(Attributes.ATTACK_DAMAGE);
                removeModifier(attributeInstance, CHESTPLATE_BONUS);
                break;
            case LEGS:
                attributeInstance = player.getAttribute(NoezAttributes.CRIT_DAMAGE.get());
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

    public static boolean isBroken(ItemStack itemStack) {
        return itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1;
    }

    @Override
    public String helmetTooltip() {
        return "noez.attack_speed_bonus";
    }

    @Override
    public String chesplateTooltip() {
        return "noez.damage_bonus";
    }

    @Override
    public String leggingsTooltip() {
        return "noez.crit_damage_bonus";
    }

    @Override
    public String bootsTooltip() {
        return "noez.crit_bonus";
    }

    @Override
    public float helmetValue() {
        return HELMET_VALUE * 100;
    }

    @Override
    public float chesplateValue() {
        return CHESTPLATE_VALUE;
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
