package net.tiramisu.noez.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tiramisu.noez.network.NoezNetwork;
import net.tiramisu.noez.network.packet.SwingC2SPacket;

import java.util.function.BiConsumer;

public abstract class ProjectileSword extends SwordItem{
    private final int cooldownTicks;
    private final BiConsumer<Player, LivingEntity> onHitEffect;

    public ProjectileSword(Properties properties, int cooldownTicks, BiConsumer<Player, LivingEntity> onHitEffect, Tier tier, int Damage, float AtkSpeed) {
        super(tier, Damage, AtkSpeed, properties);
        this.cooldownTicks = cooldownTicks;
        this.onHitEffect = onHitEffect;
        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        eventBus.register(this);
    }

    public abstract void onSwing(Player player, ItemStack stack);

    public boolean isBroken(ItemStack itemStack){
        return itemStack.getDamageValue() == itemStack.getMaxDamage() - 1;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (isBroken(stack))
            return super.hurtEnemy(stack, target, attacker);
        if (attacker instanceof Player player && onHitEffect != null) {
            onHitEffect.accept(player, target);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @SubscribeEvent
    public void onPlayerSwing(PlayerInteractEvent.LeftClickEmpty event) {
        if (isBroken(event.getItemStack()))
            return;
        if (event.getEntity().level().isClientSide) {
            NoezNetwork.CHANNEL.sendToServer(new SwingC2SPacket(cooldownTicks));
        }
    }

    @SubscribeEvent
    public void onHit(AttackEntityEvent event) {
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();
        if (isBroken(heldItem))
            return;
        if (heldItem.getItem() instanceof ProjectileSword && !player.getCooldowns().isOnCooldown(this)) {
            onSwing(player, heldItem);
            player.getCooldowns().addCooldown(this, cooldownTicks);
        }
    }

    @SubscribeEvent
    public void onHitBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();
        if (isBroken(heldItem))
            return;
        if (heldItem.getItem() instanceof ProjectileSword && !player.getCooldowns().isOnCooldown(this)) {
            onSwing(player, heldItem);
            player.getCooldowns().addCooldown(this, cooldownTicks);
        }
    }

    public int getCooldownTicks() {
        return this.cooldownTicks;
    }
}
