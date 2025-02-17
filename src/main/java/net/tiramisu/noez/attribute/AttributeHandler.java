package net.tiramisu.noez.attribute;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.TradeWithVillagerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tiramisu.noez.item.NoezItems;
import net.tiramisu.noez.util.NoezTags;

import java.util.List;

@Mod.EventBusSubscriber
public class AttributeHandler {
    @SubscribeEvent
    public static void reputationGainOnRaiderKill(LivingDeathEvent event) {
        LivingEntity target = event.getEntity();
        Entity killer = event.getSource().getEntity();
        if (target.getType().is(NoezTags.Mobs.RAIDER) && killer instanceof Player player && player.level() instanceof ServerLevel) {
            increaseReputation(player, 5);
        }
    }

    @SubscribeEvent
    public static void lowReputationLockTrade(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        if (event.getTarget() instanceof Villager villager && !player.level().isClientSide) {
            double reputation = player.getAttributeValue(NoezAttributes.REPUTATION.get());
            if (reputation <= -100) {
                villager.setTradingPlayer(null);
                villager.playSound(SoundEvents.VILLAGER_NO);
                villager.setTradingPlayer(null);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void ironGolemHelpPlayer(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        Entity entity = event.getSource().getEntity();
        if (target instanceof Player player && entity instanceof LivingEntity attacker) {
            if (player.getAttribute(NoezAttributes.REPUTATION.get()).getValue() >= 500)
                alertIronGolems(attacker.level(), attacker.blockPosition(), attacker);
        }
    }

    @SubscribeEvent
    public static void reputationGainOnVillagerTrade(TradeWithVillagerEvent event) {
        Player player = event.getEntity();
        boolean isPositiveTrade = event.getMerchantOffer().getResult().getCount() > 0;
        if (isPositiveTrade) {
            increaseReputation(player, 1);
        }
    }

    private static void alertIronGolems(Level level, BlockPos alertPos, LivingEntity target) {
        List<IronGolem> golems = level.getEntitiesOfClass(IronGolem.class,
                new AABB(alertPos).inflate(50));
        for (IronGolem golem : golems) {
            if (golem.getTarget() == null)
                golem.setTarget(target);
        }
    }

    private static void increaseReputation(Player player, int amount) {
        if (player.getAttributes().hasAttribute(NoezAttributes.REPUTATION.get())) {
            double reputation = player.getAttributeValue(NoezAttributes.REPUTATION.get());
            player.getAttribute(NoezAttributes.REPUTATION.get()).setBaseValue(reputation + amount);
            System.out.println("Player's Reputation is: " + player.getAttribute(NoezAttributes.REPUTATION.get()).getValue());
        }
    }

    private static void decreaseReputation(Player player, int amount) {
        if (player.getAttributes().hasAttribute(NoezAttributes.REPUTATION.get())) {
            double reputation = player.getAttributeValue(NoezAttributes.REPUTATION.get());
            player.getAttribute(NoezAttributes.REPUTATION.get()).setBaseValue(reputation - amount);
            System.out.println("Player's Reputation is: " + player.getAttribute(NoezAttributes.REPUTATION.get()).getValue());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onLivingHurtMagic(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        Entity attackerEntity = source.getEntity();
        if (isMagic(source)) {
            if (attackerEntity instanceof LivingEntity attacker && attacker.getAttributeValue(NoezAttributes.MAGIC_DAMAGE.get()) != 0) {
                double bonus = attacker.getAttributeValue(NoezAttributes.MAGIC_DAMAGE.get());
                float boostedDamage = event.getAmount() + applyBonus(event.getAmount(), bonus) ;
                event.setAmount(boostedDamage);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (isProjectile(event.getSource())) {
            double reduction = livingEntity.getAttributeValue(NoezAttributes.PROJECTILE_REDUCTION.get());
            event.setAmount(applyReduction(event.getAmount(), reduction));
        }
        if (isMagic(event.getSource())) {
            double reduction = livingEntity.getAttributeValue(NoezAttributes.MAGIC_REDUCTION.get());
            event.setAmount(applyReduction(event.getAmount(), reduction));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity livingEntity = event.getEntity();
            try {
            double healRegen = livingEntity.getAttribute(NoezAttributes.HEALTH_REGENERATION.get()).getValue();
            livingEntity.heal(healPerTick(healRegen));
        } catch (Exception e) {
            System.out.println("No Health Regenerate Attribute for " + livingEntity.getType());
        }

        try {
            double manaRegen = livingEntity.getAttribute(NoezAttributes.MANA_REGENERATION.get()).getValue();
            livingEntity.getCapability(NoezCapacity.MANA).ifPresent(mana ->
                    mana.addFloatMana(manaPerTick(manaRegen)));
        } catch (Exception e) {
            System.out.println("No Mana Regenerate Attribute for " + livingEntity.getType());
        }
    }

    private static float healPerTick(double value) {
        return (float) (value * 0.005);
    }

    private static float manaPerTick(double value) {
        return (float) (value * 0.005);
    }

    private static float applyReduction(float damage, double reduction) {
        return (float) (damage * (1.0 - reduction / 100.0));
    }

    private static float applyBonus(float damage, double points) {
        return (float) (damage * (1.0 - points / 100.0));
    }

    private static boolean isMagic(DamageSource source) {
        return source.getMsgId().equals("magic") || source.getMsgId().equals("indirectMagic");
    }

    private static boolean isProjectile(DamageSource source) {
        return source.getMsgId().equals("arrow") || source.getMsgId().equals("thrown");
    }
}