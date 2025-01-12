package net.tiramisu.noez.item.armors;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tiramisu.noez.attribute.NoezAttributes;
import net.tiramisu.noez.client.renderer.TwilightWitchHatRenderer;
import net.tiramisu.noez.item.ArmorAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class TwilightWitchHat extends ArmorItem implements ArmorAttribute, GeoItem {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final UUID TWILIGHT_BONUS = UUID.fromString("12304267-e89b-12d3-a456-426614173000");
    private static final float MAGIC_BONUS = 45;
    
    public TwilightWitchHat(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof TwilightWitchHat && !isBroken(player.getItemBySlot(EquipmentSlot.HEAD))) {
            EquipmentSlot slot = this.getType().getSlot();
            applyBonus(player, slot);
        } else {
            removeBonus(player, EquipmentSlot.HEAD);
        }
    }

    private void applyBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;
        AttributeModifier modifier;
        if (Objects.requireNonNull(slot) == EquipmentSlot.HEAD) {
            attributeInstance = player.getAttribute(NoezAttributes.MAGIC_DAMAGE.get());
            modifier = new AttributeModifier(TWILIGHT_BONUS, "Twilight bonus", MAGIC_BONUS, AttributeModifier.Operation.ADDITION);
            applyModifier(attributeInstance, modifier);
        }
    }

    private void removeBonus(Player player, EquipmentSlot slot) {
        AttributeInstance attributeInstance;
        if (Objects.requireNonNull(slot) == EquipmentSlot.HEAD) {
            attributeInstance = player.getAttribute(NoezAttributes.MAGIC_DAMAGE.get());
            removeModifier(attributeInstance, TWILIGHT_BONUS);
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
        return "noez.twilight_witch_hat.tooltip";
    }

    @Override
    public void setTooltipID(String tooltipID) {

    }

    @Override
    public String helmetTooltip() {
        return "noez.magic_damage_bonus";
    }

    @Override
    public String chesplateTooltip() {
        return "";
    }

    @Override
    public String leggingsTooltip() {
        return "";
    }

    @Override
    public String bootsTooltip() {
        return "";
    }

    @Override
    public float helmetValue() {
        return MAGIC_BONUS;
    }

    @Override
    public float chesplateValue() {
        return 0;
    }

    @Override
    public float leggingsValue() {
        return 0;
    }

    @Override
    public float bootsValue() {
        return 0;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private TwilightWitchHatRenderer renderer;
            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null)
                    this.renderer = new TwilightWitchHatRenderer();

                if (equipmentSlot == EquipmentSlot.HEAD) {
                    this.renderer.prepForRender(livingEntity, itemStack, EquipmentSlot.HEAD, original);
                }
                renderer.prepForRender(livingEntity, itemStack, EquipmentSlot.HEAD, original);
                return this.renderer;
            }
        });
    }
}
