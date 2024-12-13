package net.tiramisu.noez.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tiramisu.noez.network.NoezNetwork;
import net.tiramisu.noez.network.SwingPacket;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.BiConsumer;

public abstract class SpellStaff extends Item {

    private static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
    private static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");

    private final double attackDamage;
    private final double attackSpeed;
    private final int cooldownTicks;
    private final BiConsumer<Player, LivingEntity> onHitEffect;

    public SpellStaff(Properties properties, double attackDamage, double attackSpeed, int cooldownTicks, @Nullable BiConsumer<Player, LivingEntity> onHitEffect) {
        super(properties);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.cooldownTicks = cooldownTicks;
        this.onHitEffect = onHitEffect;
        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        eventBus.register(this);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED,
                    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));

            return builder.build();
        }
        return super.getDefaultAttributeModifiers(slot);
    }

    public abstract void onSwing(Player player, ItemStack stack);
    public abstract void onActivate(Player player, ItemStack stack, int cooldownTicks);

    public boolean isBroken(ItemStack itemStack) {
        return itemStack.getDamageValue() == itemStack.getMaxDamage() - 1;
    }

    @SubscribeEvent
    public void onHitEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof SpellStaff)) return;
        if (isBroken(heldItem)) return;
        LivingEntity target = event.getTarget() instanceof LivingEntity ? (LivingEntity) event.getTarget() : null;
        if (target != null && onHitEffect != null) {
            onHitEffect.accept(player, target);
        }
        onSwing(player, heldItem);
    }

    @SubscribeEvent
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof SpellStaff)) return;
        if (isBroken(heldItem)) return;
        onSwing(player, heldItem);
    }

    @SubscribeEvent
    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        ItemStack heldItem = event.getItemStack();
        if (!(heldItem.getItem() instanceof SpellStaff)) return;
        if (isBroken(heldItem)) return;
        if (!player.level().isClientSide && heldItem.getItem() instanceof SpellStaff spellStaff) {
            NoezNetwork.CHANNEL.sendToServer(new SwingPacket(0));
        }
    }

    @SubscribeEvent
    public void abilityUse(PlayerInteractEvent.RightClickItem event){
        Player player = event.getEntity();
        ItemStack itemStack = player.getUseItem();
        ItemStack heldItem = event.getItemStack();
        if (!(heldItem.getItem() instanceof SpellStaff)) return;
        if (isBroken(heldItem)) return;
        onActivate(player, itemStack, this.cooldownTicks);
    }

    public int getCooldownTicks() {
        return cooldownTicks;
    }
}
