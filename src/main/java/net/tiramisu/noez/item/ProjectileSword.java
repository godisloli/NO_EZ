package net.tiramisu.noez.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.BiConsumer;

public abstract class ProjectileSword extends SwordItem {
    private final int cooldownTicks; // Cooldown in ticks
    private final BiConsumer<Player, LivingEntity> onHitEffect;

    public ProjectileSword(Properties properties, int cooldownTicks, BiConsumer<Player, LivingEntity> onHitEffect) {
        super(Tiers.NETHERITE, 3, -2.4F, properties);
        this.cooldownTicks = cooldownTicks;
        this.onHitEffect = onHitEffect;
        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        eventBus.register(this);
    }

    protected abstract void onSwing(Player player, ItemStack stack);

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && onHitEffect != null) {
            onHitEffect.accept(player, target);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @SubscribeEvent
    public void onPlayerSwing(PlayerInteractEvent.LeftClickEmpty event) {
        handleSwing(event);
    }

    @SubscribeEvent
    public void onPlayerSwingBlock(PlayerInteractEvent.LeftClickBlock event) {
        handleSwing(event);
    }

    private void handleSwing(PlayerInteractEvent event) {
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();
        if (heldItem.getItem() instanceof ProjectileSword && !player.getCooldowns().isOnCooldown(this)) {
            onSwing(player, heldItem);
            player.getCooldowns().addCooldown(this, cooldownTicks);
        }
    }

    public int getCooldownTicks() {
        return this.cooldownTicks;
    }
}
