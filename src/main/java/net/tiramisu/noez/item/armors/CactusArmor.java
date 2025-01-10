package net.tiramisu.noez.item.armors;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.effect.NoezEffects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mod.EventBusSubscriber
public class CactusArmor extends ArmorItem {
    private String toolTipId = "none";

    public CactusArmor(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (hasFullCactusArmorSet(player)) {
            setTooltipID("full");
        }

        if (hasHalfCactusArmorSet(player)) {
            setTooltipID("half");
        }

        if (!hasHalfCactusArmorSet(player) && !hasFullCactusArmorSet(player)) {
            setTooltipID("none");
        }
    }

    @SubscribeEvent
    public static void thornyCactus(LivingHurtEvent event) {
        Entity entity = event.getEntity();
        Entity sourceEntity = event.getSource().getEntity();
        float damage = event.getAmount();
        if (entity instanceof Player player && sourceEntity instanceof LivingEntity attacker) {
            if (hasHalfCactusArmorSet(player)) {
                attacker.hurt(player.damageSources().thorns(player),damage * 0.15f);
            }
            if (hasFullCactusArmorSet(player)) {
                attacker.hurt(player.damageSources().thorns(player),damage * 0.25f);
                attacker.addEffect(new MobEffectInstance(NoezEffects.WOUND.get(), 3 * 20));
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("noez.cactus_" + toolTipId + ".tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.cactus_" + toolTipId + ".tooltip2"));
    }

    private static boolean hasFullCactusArmorSet(Player player) {
        boolean hasHelmet = player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof CactusArmor && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD));
        boolean hasChestplate = player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof CactusArmor && !isBroken(player.getItemBySlot(EquipmentSlot.CHEST));
        boolean hasLeggings = player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof CactusArmor && !isBroken(player.getItemBySlot(EquipmentSlot.LEGS));
        boolean hasBoots = player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof CactusArmor && !isBroken(player.getItemBySlot(EquipmentSlot.FEET));

        return hasHelmet && hasChestplate && hasLeggings && hasBoots;
    }

    private static boolean hasHalfCactusArmorSet(Player player) {
        int count = 0;
        count += player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof CactusArmor && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof CactusArmor && !isBroken(player.getItemBySlot(EquipmentSlot.CHEST)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof CactusArmor && !isBroken(player.getItemBySlot(EquipmentSlot.LEGS)) ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof CactusArmor && !isBroken(player.getItemBySlot(EquipmentSlot.FEET)) ? 1 : 0;
        return count >= 2 && count < 4;
    }

    public static boolean isBroken(ItemStack itemStack) {
        return itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1;
    }
    
    private void setTooltipID(String id) {
        this.toolTipId = id;
    }
}
