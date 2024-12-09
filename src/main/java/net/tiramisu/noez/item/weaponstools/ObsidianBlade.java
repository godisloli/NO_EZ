package net.tiramisu.noez.item.weaponstools;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.tiramisu.noez.effect.NoezEffects;
import net.tiramisu.noez.item.Critable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ObsidianBlade extends SwordItem implements Critable {
    private final int BLEED_DURATION = 3; // seconds
    private final int COOLDOWN = 12;
    private static final double CRIT_CHANCE = 0.35;
    private static final double CRIT_DAMAGE = 1.75;

    public ObsidianBlade(Tier tier, int Damage, float AttackSpeed, Properties properties){
        super(tier, Damage, AttackSpeed, properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("noez.obsidian_blade.tooltip1"));
        pTooltipComponents.add(Component.translatable("noez.obsidian_blade.tooltip2"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide && attacker instanceof Player player) {
            if (!player.getCooldowns().isOnCooldown(this)) {
                target.addEffect(new MobEffectInstance(NoezEffects.BLEED.get(), BLEED_DURATION * 20, 2));
                player.getCooldowns().addCooldown(this, COOLDOWN * 20);
            }
            float baseDamage = (float) this.getDamage();
            float ignoredArmor = target.getArmorValue() * 0.3f; // 30% of armor
            float armorReduction = ignoredArmor / 2; // Each point of armor reduces damage by 0.5 (vanilla formula)
            float finalDamage = baseDamage + armorReduction;
            target.hurt(attacker.damageSources().mobAttack(attacker), finalDamage);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public double getCritChance() {
        return CRIT_CHANCE;
    }

    @Override
    public double getCritDamageAmplifier() {
        return CRIT_DAMAGE;
    }
}
