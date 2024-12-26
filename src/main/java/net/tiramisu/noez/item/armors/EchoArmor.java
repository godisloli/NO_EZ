package net.tiramisu.noez.item.armors;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.item.ArmorAttribute;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mod.EventBusSubscriber
public class EchoArmor extends ArmorItem implements ArmorAttribute {

    private String toolTipId = "none";
    private static int RADIUS = 25;

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
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("noez.echo_" + toolTipId + ".tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.echo_" + toolTipId + ".tooltip2"));

    }

    private static boolean hasFullEchoArmorSet(Player player) {
        boolean hasHelmet = player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof EchoArmor;
        boolean hasChestplate = player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof EchoArmor;
        boolean hasLeggings = player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof EchoArmor;
        boolean hasBoots = player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof EchoArmor;

        return hasHelmet && hasChestplate && hasLeggings && hasBoots;
    }

    private static boolean hasHalfEchoArmorSet(Player player) {
        int count = 0;
        count += player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof EchoArmor ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof EchoArmor ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof EchoArmor ? 1 : 0;
        count += player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof EchoArmor ? 1 : 0;

        return count >= 2 && count < 4;
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (hasFullEchoArmorSet(player)) {
            applyGlowingEffectInRadius(player, level, RADIUS);
            setTooltipID("full");
        }
        if (hasHalfEchoArmorSet(player)) {
            setTooltipID("half");
        }
        if (!hasHalfEchoArmorSet(player) && !hasFullEchoArmorSet(player))
            setTooltipID("none");
    }

    private void applyGlowingEffectInRadius(Player player, Level level, float radius) {
        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(radius), entity -> entity != player);
        for (LivingEntity entity : nearbyEntities) {
            if (!entity.hasEffect(MobEffects.GLOWING)) {
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0, false, false));
                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SCULK_CLICKING, SoundSource.NEUTRAL, 2.5f, 1f);
            }
        }
    }
}
