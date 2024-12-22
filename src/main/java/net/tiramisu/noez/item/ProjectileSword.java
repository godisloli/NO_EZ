package net.tiramisu.noez.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tiramisu.noez.network.NoezNetwork;
import net.tiramisu.noez.network.packet.SwingC2SPacket;

import java.util.function.BiConsumer;

public abstract class ProjectileSword extends SwordItem {
    private final int cooldownTicks;
    private final BiConsumer<Player, LivingEntity> onHitEffect;

    public ProjectileSword(Properties properties, int cooldownTicks, BiConsumer<Player, LivingEntity> onHitEffect, Tier tier, int damage, float attackSpeed) {
        super(tier, damage, attackSpeed, properties);
        this.cooldownTicks = cooldownTicks;
        this.onHitEffect = onHitEffect;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public abstract void onSwing(Player player, ItemStack stack);

    public abstract boolean matches(ItemStack stack);


    public boolean isBroken(ItemStack itemStack) {
        return itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (isBroken(stack)) return super.hurtEnemy(stack, target, attacker);

        if (attacker instanceof Player player && onHitEffect != null) {
            onHitEffect.accept(player, target);
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    @SubscribeEvent
    public void onPlayerSwing(PlayerInteractEvent.LeftClickEmpty event) {
        ItemStack stack = event.getItemStack();

        if (isBroken(stack) || !matches(stack)) return;

        if (event.getEntity().level().isClientSide) {
            NoezNetwork.CHANNEL.sendToServer(new SwingC2SPacket(cooldownTicks));
        }
    }

    @SubscribeEvent
    public void onHit(AttackEntityEvent event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();

        if (isBroken(stack) || !matches(stack)) return;

        if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
            onSwing(player, stack);
            player.getCooldowns().addCooldown(stack.getItem(), cooldownTicks);
        }
    }

    @SubscribeEvent
    public void onHitBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();

        if (isBroken(stack) || !matches(stack)) return;

        if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
            onSwing(player, stack);
            player.getCooldowns().addCooldown(stack.getItem(), cooldownTicks);
        }
    }

    public int getCooldownTicks() {
        return this.cooldownTicks;
    }
}
