package net.tiramisu.noez.item.armors;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.attribute.NoezAttributes;
import net.tiramisu.noez.item.ArmorAttribute;
import net.tiramisu.noez.item.NoezItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Mod.EventBusSubscriber
public class DruidArmor extends ArmorItem implements ArmorAttribute {
    private String toolTipId = "none";
    private static final int HELMET_VALUE = 10;
    private static final float CHESTPLATE_VALUE = 1;
    private static final int LEGGINGS_VALUE = 1;
    private static final float BOOTS_VALUE = 15;
    private static final UUID HELMET_BONUS = UUID.fromString("11111112-1111-1111-1111-111111111111");
    private static final UUID CHESTPLATE_BONUS = UUID.fromString("22232222-2222-2222-2222-222222222222");
    private static final UUID LEGGINGS_BONUS = UUID.fromString("33333433-3333-3333-3333-333333333333");
    private static final UUID BOOTS_BONUS = UUID.fromString("44444445-4444-4444-4444-444444444444");
    private final List<Wolf> summonedWolves = new ArrayList<>();
    private static final Map<Player, List<Wolf>> playerSummonedWolves = new HashMap<>();
    private static final int COOLDOWN = 15 * 20;
    private static final UUID PET_ATTACK_DAMAGE_BONUS_UUID = UUID.fromString("12345678-1234-5678-1234-567812345678");
    private static final double DAMAGE_BONUS_MULTIPLIER = 0.5;
    
    public DruidArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("noez.druid_" + toolTipId + ".tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.druid_" + toolTipId + ".tooltip2"));
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (hasFullDruidArmorSet(player)) {
            setTooltipID("full");
            halfSetBonus(player);
            fullSetBonus(player);
        }

        if (hasHalfDruidArmorSet(player)) {
            setTooltipID("half");
            halfSetBonus(player);
        }

        if (!hasHalfDruidArmorSet(player) && !hasFullDruidArmorSet(player)) {
            setTooltipID("none");
            level.getEntitiesOfClass(TamableAnimal.class, player.getBoundingBox().inflate(50),
                    pet -> pet.isTame() && pet.getOwner() == player
            ).forEach(DruidArmor::removeAttackDamageBonus);
        }

        if (player.isDeadOrDying() || !hasFullDruidArmorSet(player)) {
            summonedWolves.forEach(wolf -> {
                if (wolf.isAlive()) {
                    wolf.discard();
                }
            });
            summonedWolves.clear();
            return;
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
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            List<Wolf> wolves = playerSummonedWolves.remove(player);
            if (wolves != null) {
                wolves.forEach(wolf -> {
                    if (wolf.isAlive()) {
                        wolf.discard();
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void logOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        List<Wolf> wolves = playerSummonedWolves.remove(player);
        if (wolves != null) {
            wolves.forEach(wolf -> {
                if (wolf.isAlive()) {
                    wolf.discard();
                }
            });
        }
    }

    private void fullSetBonus(Player player) {
        List<Wolf> wolves = playerSummonedWolves.computeIfAbsent(player, k -> new ArrayList<>());

        wolves.removeIf(w -> !w.isAlive() || !hasFullDruidArmorSet(player) || w.getOwner() == null || !w.getOwner().isAlive());

        if (wolves.size() >= 4) {
            return;
        }

        boolean onCooldown = player.getCooldowns().isOnCooldown(NoezItems.DRUID_CHESTPLATE.get()) ||
                player.getCooldowns().isOnCooldown(NoezItems.DRUID_HELMET.get()) ||
                player.getCooldowns().isOnCooldown(NoezItems.DRUID_BOOTS.get()) ||
                player.getCooldowns().isOnCooldown(NoezItems.DRUID_LEGGINGS.get());

        if (player.level() instanceof ServerLevel serverLevel && !onCooldown) {
            player.getCooldowns().addCooldown(NoezItems.DRUID_HELMET.get(), COOLDOWN);
            player.getCooldowns().addCooldown(NoezItems.DRUID_CHESTPLATE.get(), COOLDOWN);
            player.getCooldowns().addCooldown(NoezItems.DRUID_LEGGINGS.get(), COOLDOWN);
            player.getCooldowns().addCooldown(NoezItems.DRUID_BOOTS.get(), COOLDOWN);
            BlockPos spawnPos = findValidSpawnPosition(player, serverLevel);
            Wolf wolf = new Wolf(EntityType.WOLF, serverLevel);
            wolf.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
            wolf.setTame(true);
            wolf.setCollarColor(DyeColor.GREEN);
            wolf.setOwnerUUID(player.getUUID());
            String name = player.getName().getString();
            wolf.setCustomName(Component.translatable("noez.druid_wolf", name));
            wolf.setPersistenceRequired();
            serverLevel.addFreshEntity(wolf);
            wolves.add(wolf);
        }
    }

    private BlockPos findValidSpawnPosition(Player player, ServerLevel level) {
        Random random = new Random();
        BlockPos playerPos = player.blockPosition();

        for (int i = 0; i < 10; i++) {
            int offsetX = random.nextInt(7) - 3;
            int offsetY = random.nextInt(7) - 3;
            int offsetZ = random.nextInt(7) - 3;
            BlockPos spawnPos = playerPos.offset(offsetX, offsetY, offsetZ);

            if (isValidSpawnLocation(spawnPos, level)) {
                return spawnPos;
            }
        }
        return playerPos;
    }

    private boolean isValidSpawnLocation(BlockPos pos, ServerLevel level) {
        BlockState blockState = level.getBlockState(pos);
        BlockState belowBlockState = level.getBlockState(pos.below());
        return !blockState.isAir() && !blockState.is(Blocks.LAVA) && !belowBlockState.isAir() && !belowBlockState.is(Blocks.LAVA) && !blockState.isSolidRender(level, pos);
    }

    private void halfSetBonus(Player player) {
        Level level = player.level();
        if (!level.isClientSide) {
            level.getEntitiesOfClass(TamableAnimal.class, player.getBoundingBox().inflate(50),
                    pet -> pet.isTame() && pet.getOwner() == player
            ).forEach(DruidArmor::applyAttackDamageBonus);
        }
    }

    public static void applyAttackDamageBonus(TamableAnimal pet) {
        AttributeInstance attackDamage = pet.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamage != null && !attackDamage.hasModifier(getDamageBonusModifier())) {
            attackDamage.addTransientModifier(getDamageBonusModifier());
        }
    }

    public static void removeAttackDamageBonus(TamableAnimal pet) {
        AttributeInstance attackDamage = pet.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamage != null && attackDamage.getModifier(PET_ATTACK_DAMAGE_BONUS_UUID) != null) {
            attackDamage.removeModifier(PET_ATTACK_DAMAGE_BONUS_UUID);
        }
    }

    private static AttributeModifier getDamageBonusModifier() {
        return new AttributeModifier(PET_ATTACK_DAMAGE_BONUS_UUID, "Druid Half Set Bonus", DAMAGE_BONUS_MULTIPLIER, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    private static boolean hasFullDruidArmorSet(Player player) {
        boolean hasHelmet = player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof DruidArmor && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD));
        boolean hasChestplate = player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof DruidArmor && !isBroken(player.getItemBySlot(EquipmentSlot.CHEST));
        boolean hasLeggings = player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof DruidArmor && !isBroken(player.getItemBySlot(EquipmentSlot.LEGS));
        boolean hasBoots = player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof DruidArmor && !isBroken(player.getItemBySlot(EquipmentSlot.FEET));

        return hasHelmet && hasChestplate && hasLeggings && hasBoots;
    }

    private static boolean hasHalfDruidArmorSet(Player player) {
        int count = 0;
        count += player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof DruidArmor && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof DruidArmor && !isBroken(player.getItemBySlot(EquipmentSlot.CHEST)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof DruidArmor && !isBroken(player.getItemBySlot(EquipmentSlot.LEGS)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof DruidArmor && !isBroken(player.getItemBySlot(EquipmentSlot.FEET)) ? 1 : 0;

        return count >= 2 && count < 4;
    }

    public static boolean isBroken(ItemStack itemStack) {
        return itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1;
    }

    private void applyBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;
        AttributeModifier modifier;

        switch (slot) {
            case HEAD:
                attributeInstance = player.getAttribute(NoezAttributes.MAGIC_DAMAGE.get());
                modifier = new AttributeModifier(HELMET_BONUS, "Druid helmet bonus", HELMET_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case CHEST:
                attributeInstance = player.getAttribute(NoezAttributes.HEALTH_REGENERATION.get());
                modifier = new AttributeModifier(CHESTPLATE_BONUS, "Druid chestplate bonus", CHESTPLATE_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case LEGS:
                attributeInstance = player.getAttribute(NoezAttributes.MANA_REGENERATION.get());
                modifier = new AttributeModifier(LEGGINGS_BONUS, "Druid leggings bonus", LEGGINGS_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
            case FEET:
                attributeInstance = player.getAttribute(NoezAttributes.TENACITY.get());
                modifier = new AttributeModifier(BOOTS_BONUS, "Druid boots bonus", BOOTS_VALUE, AttributeModifier.Operation.ADDITION);
                applyModifier(attributeInstance, modifier);
                break;
        }
    }

    private void removeBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;

        switch (slot) {
            case HEAD:
                attributeInstance = player.getAttribute(NoezAttributes.MAGIC_DAMAGE.get());
                removeModifier(attributeInstance, HELMET_BONUS);
                break;
            case CHEST:
                attributeInstance = player.getAttribute(NoezAttributes.HEALTH_REGENERATION.get());
                removeModifier(attributeInstance, CHESTPLATE_BONUS);
                break;
            case LEGS:
                attributeInstance = player.getAttribute(NoezAttributes.MANA_REGENERATION.get());
                removeModifier(attributeInstance, LEGGINGS_BONUS);
                break;
            case FEET:
                attributeInstance = player.getAttribute(NoezAttributes.TENACITY.get());
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

    @Override
    public String getTooltipID() {
        return "noez.druid_" + toolTipId;
    }

    @Override
    public void setTooltipID(String tooltipID) {
        this.toolTipId = tooltipID;
    }

    @Override
    public String helmetTooltip() {
        return "noez.magic_damage_bonus";
    }

    @Override
    public String chesplateTooltip() {
        return "noez.heal_regen_bonus";
    }

    @Override
    public String leggingsTooltip() {
        return "noez.mana_regen_bonus";
    }

    @Override
    public String bootsTooltip() {
        return "noez.tenacity_bonus";
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
