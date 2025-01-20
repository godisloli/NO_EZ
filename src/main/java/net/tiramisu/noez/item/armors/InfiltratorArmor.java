package net.tiramisu.noez.item.armors;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.attribute.NoezAttributes;
import net.tiramisu.noez.item.ArmorAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.HashMap;

@Mod.EventBusSubscriber
public class InfiltratorArmor extends ArmorItem implements ArmorAttribute {
    private String toolTipId = "none";
    private static final float HELMET_VALUE = 0.02f;
    private static final int CHESTPLATE_VALUE = 25;
    private static final int LEGGINGS_VALUE = 15;
    private static final int BOOTS_VALUE = 20;
    private static final float HALF_SET_VALUE = 75;
    private static final UUID HELMET_BONUS = UUID.fromString("12113111-1121-1111-1111-111111111111");
    private static final UUID CHESTPLATE_BONUS = UUID.fromString("23222422-2622-2222-2222-122222225222");
    private static final UUID LEGGINGS_BONUS = UUID.fromString("34333363-3363-3333-3333-333333336333");
    private static final UUID BOOTS_BONUS = UUID.fromString("42445444-4444-4444-4444-444444244444");
    private static final UUID HALF_SET_BONUS = UUID.fromString("52445444-5544-5544-5445-555544244444");
    private static final HashMap<UUID, Integer> invisibilityTimers = new HashMap<>();

    public InfiltratorArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("noez.infiltrator_" + toolTipId + ".tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.infiltrator_" + toolTipId + ".tooltip2"));
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (hasFullInfiltratorArmorSet(player)) {
            setTooltipID("full");
            halfSetBonus(player);
        }

        if (hasHalfInfiltratorArmorSet(player)) {
            setTooltipID("half");
            halfSetBonus(player);
        }

        if (!hasHalfInfiltratorArmorSet(player) && !hasFullInfiltratorArmorSet(player)) {
            setTooltipID("none");
            removeModifier(player.getAttribute(NoezAttributes.CRIT_DAMAGE.get()), HALF_SET_BONUS);
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

    private void halfSetBonus(Player player) {
        AttributeModifier attributeModifier = new AttributeModifier(HALF_SET_BONUS, "Infiltrator half set bonus", HALF_SET_VALUE, AttributeModifier.Operation.ADDITION);
        applyModifier(player.getAttribute(NoezAttributes.CRIT_DAMAGE.get()), attributeModifier);
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player && (hasHalfInfiltratorArmorSet(player) || hasFullInfiltratorArmorSet(player))) {
            event.setCanceled(true);
            player.fallDistance = 0;
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;
        if (InfiltratorArmor.hasFullInfiltratorArmorSet(player)) {
            invisibilityTimers.put(player.getUUID(), 5);
            player.heal(event.getEntity().getMaxHealth() * 0.1f);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        UUID playerUUID = player.getUUID();

        if (invisibilityTimers.containsKey(playerUUID)) {
            int remainingTicks = invisibilityTimers.get(playerUUID);

            applyInvisibility(player);

            if (remainingTicks <= 1) {
                invisibilityTimers.remove(playerUUID);
            } else {
                invisibilityTimers.put(playerUUID, remainingTicks - 1);
            }
        }
    }

    private static void applyInvisibility(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 100, 0));
    }

    private static boolean hasFullInfiltratorArmorSet(Player player) {
        boolean hasHelmet = player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof InfiltratorArmor && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD));
        boolean hasChestplate = player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof InfiltratorArmor && !isBroken(player.getItemBySlot(EquipmentSlot.CHEST));
        boolean hasLeggings = player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof InfiltratorArmor && !isBroken(player.getItemBySlot(EquipmentSlot.LEGS));
        boolean hasBoots = player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof InfiltratorArmor && !isBroken(player.getItemBySlot(EquipmentSlot.FEET));

        return hasHelmet && hasChestplate && hasLeggings && hasBoots;
    }

    private static boolean hasHalfInfiltratorArmorSet(Player player) {
        int count = 0;
        count += player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof InfiltratorArmor && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof InfiltratorArmor && !isBroken(player.getItemBySlot(EquipmentSlot.CHEST)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof InfiltratorArmor && !isBroken(player.getItemBySlot(EquipmentSlot.LEGS)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof InfiltratorArmor && !isBroken(player.getItemBySlot(EquipmentSlot.FEET)) ? 1 : 0;

        return count >= 2 && count < 4;
    }

    private void applyBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;
        AttributeModifier modifier;

        switch (slot) {
            case HEAD:
                attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                modifier = new AttributeModifier(HELMET_BONUS, "Infiltrator helmet bonus", HELMET_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case CHEST:
                attributeInstance = player.getAttribute(NoezAttributes.CRIT_CHANCE.get());
                modifier = new AttributeModifier(CHESTPLATE_BONUS, "Infiltrator chestplate bonus", CHESTPLATE_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case LEGS:
                attributeInstance = player.getAttribute(NoezAttributes.CRIT_CHANCE.get());
                modifier = new AttributeModifier(LEGGINGS_BONUS, "Infiltrator leggings bonus", LEGGINGS_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case FEET:
                attributeInstance = player.getAttribute(NoezAttributes.CRIT_DAMAGE.get());
                modifier = new AttributeModifier(BOOTS_BONUS, "Infiltrator boots bonus", BOOTS_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
        }
    }

    private void removeBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;

        switch (slot) {
            case HEAD:
                attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                removeModifier(attributeInstance, HELMET_BONUS);
                break;
            case CHEST:
                attributeInstance = player.getAttribute(NoezAttributes.CRIT_CHANCE.get());
                removeModifier(attributeInstance, CHESTPLATE_BONUS);
                break;
            case LEGS:
                attributeInstance = player.getAttribute(NoezAttributes.CRIT_CHANCE.get());
                removeModifier(attributeInstance, LEGGINGS_BONUS);
                break;
            case FEET:
                attributeInstance = player.getAttribute(NoezAttributes.CRIT_DAMAGE.get());
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
        return "noez.infiltrator_" + toolTipId;
    }

    @Override
    public void setTooltipID(String tooltipID) {
        this.toolTipId = tooltipID;
    }

    @Override
    public String helmetTooltip() {
        return "noez.movement_speed_bonus";
    }

    @Override
    public String chesplateTooltip() {
        return "noez.crit_bonus";
    }

    @Override
    public String leggingsTooltip() {
        return "noez.crit_bonus";
    }

    @Override
    public String bootsTooltip() {
        return "noez.crit_damage_bonus";
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
        return LEGGINGS_VALUE;
    }

    @Override
    public float bootsValue() {
        return BOOTS_VALUE;
    }
}
