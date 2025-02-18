package net.tiramisu.noez.item.arsenal;

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
    private final static int BLEED_DURATION = 3; // seconds
    private final static int COOLDOWN = 12;
    private final static double CRIT_CHANCE = 0.35;
    private final static double CRIT_DAMAGE = 1.75;
    private boolean ALWAYS_CRIT = false;

    public ObsidianBlade(Tier tier, int Damage, float AttackSpeed, Properties properties) {
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
        if (!target.level().isClientSide() && !(stack.getDamageValue() == stack.getMaxDamage() - 1)) {
            if (attacker instanceof Player player && !player.getCooldowns().isOnCooldown(this)) {
                target.addEffect(new MobEffectInstance(NoezEffects.BLEED.get(), BLEED_DURATION * 20, 2));
                player.getCooldowns().addCooldown(this, COOLDOWN * 20);
            }
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

    @Override
    public boolean isAlwaysCrit(){
        return ALWAYS_CRIT;
    }

    @Override
    public void setAlwaysCrit(boolean value){
        this.ALWAYS_CRIT = value;
    }
}
